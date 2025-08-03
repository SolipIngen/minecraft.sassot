package solipingen.sassot.item;

import com.mojang.serialization.Codec;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.component.type.BlocksAttacksComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import solipingen.sassot.sound.ModSoundEvents;

import java.util.List;
import java.util.Optional;


public class ModShieldItem extends ShieldItem {
    private final ToolMaterial material;;
    private final float unyieldingModifier;
    private final boolean isFramed;

    
    public ModShieldItem(ToolMaterial material, float minBreakDamage, boolean isFramedShield, float blockDelaySeconds, float disabledScale, float unyieldingModifier, Item.Settings settings) {
        super(settings.maxDamage(MathHelper.ceil((isFramedShield ? 0.8f : 1.2f)*material.durability()))
                .repairable(isFramedShield ? ToolMaterial.WOOD.repairItems() : material.repairItems())
                .enchantable(material.enchantmentValue())
                .equippableUnswappable(EquipmentSlot.OFFHAND)
                .component(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT)
                .component(DataComponentTypes.BLOCKS_ATTACKS, ModShieldItem.createShieldComponent(material, minBreakDamage, isFramedShield, blockDelaySeconds, disabledScale))
                .component(DataComponentTypes.BREAK_SOUND, SoundEvents.ITEM_SHIELD_BREAK));
        this.material = material;
        this.unyieldingModifier = unyieldingModifier;
        this.isFramed = isFramedShield;
    }

    public static BlocksAttacksComponent createShieldComponent(ToolMaterial material, float minBreakDamage, boolean isFramedShield, float blockDelaySeconds, float disabledScale) {
        return new BlocksAttacksComponent(blockDelaySeconds, disabledScale,
                List.of(new BlocksAttacksComponent.DamageReduction(90.0f, Optional.empty(), 0.0f, 1.0f)),
                new BlocksAttacksComponent.ItemDamage(minBreakDamage, 0.0f, 1.0f), Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                Optional.of(ModShieldItem.getHitSoundEvent(material, isFramedShield)), Optional.of(SoundEvents.ITEM_SHIELD_BREAK));
    }

    public float getUnyieldingModifier() {
        return this.unyieldingModifier;
    }

    public static RegistryEntry<SoundEvent> getHitSoundEvent(ToolMaterial material, boolean isFramed) {
        if (isFramed || material == ToolMaterial.WOOD) {
            return SoundEvents.ITEM_SHIELD_BLOCK;
        }
        else {
            if (material == ModToolMaterials.COPPER) {
                return ModSoundEvents.COPPER_SHIELD_BLOCK;
            }
            else if (material == ToolMaterial.GOLD) {
                return ModSoundEvents.GOLDEN_SHIELD_BLOCK;
            }
            else if (material == ToolMaterial.IRON) {
                return ModSoundEvents.IRON_SHIELD_BLOCK;
            }
            else if (material == ToolMaterial.DIAMOND) {
                return ModSoundEvents.DIAMOND_SHIELD_BLOCK;
            }
            return ModSoundEvents.NETHERITE_SHIELD_BLOCK;
        }
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        if (this.isFramed && this.material == ToolMaterial.NETHERITE) {
            World world = entity.getWorld();
            world.spawnEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Items.NETHERITE_SCRAP, 4)));
        }
        else {
            super.onItemEntityDestroyed(entity);
        }
    }


    public enum State implements StringIdentifiable {
        DEFAULT("default"),
        BLOCKING("blocking"),
        CLOAKING("cloaking");

        public static final Codec<ModShieldItem.State> CODEC = StringIdentifiable.createCodec(ModShieldItem.State::values);
        private final String name;

        State(final String name) {
            this.name = name;
        }

        public String asString() {
            return this.name;
        }
    }


}
