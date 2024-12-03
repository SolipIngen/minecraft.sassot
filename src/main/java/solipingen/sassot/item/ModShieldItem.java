package solipingen.sassot.item;

import net.minecraft.component.DataComponentTypes;

import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import solipingen.sassot.sound.ModSoundEvents;


public class ModShieldItem extends ShieldItem {
    public final float minDamageToBreak;
    private final ToolMaterial material;
    private final int enchantability;
    private final boolean isFramed;
    private final Ingredient repairIngredient;
    private final int disabledTicks;
    private final float unyieldingModifier;

    
    public ModShieldItem(ToolMaterial material, float minBreakDamage, boolean isFramedShield, int disabledTicks, float unyieldingModifier, Item.Settings settings) {
        super(settings.maxDamage(MathHelper.ceil((isFramedShield ? 0.8f : 1.2f)*material.getDurability())));
        this.material = material;
        this.minDamageToBreak = minBreakDamage;
        this.isFramed = isFramedShield;
        this.enchantability = this.material.getEnchantability();
        this.repairIngredient = this.isFramed ? ToolMaterials.WOOD.getRepairIngredient() : this.material.getRepairIngredient();
        this.disabledTicks = disabledTicks;
        this.unyieldingModifier = unyieldingModifier;
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        DyeColor dyeColor = stack.get(DataComponentTypes.BASE_COLOR);
        if (dyeColor != null) {
            return this.getTranslationKey() + "." + dyeColor.getName();
        }
        return super.getTranslationKey(stack);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.repairIngredient.test(ingredient) || super.canRepair(stack, ingredient);
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    public float getMinDamageToBreak() {
        return this.minDamageToBreak;
    }

    public int getDisabledTicks() {
        return this.disabledTicks;
    }

    public float getUnyieldingModifier() {
        return this.unyieldingModifier;
    }

    public boolean getIsFramedShield() {
        return this.isFramed;
    }

    public SoundEvent getHitSoundEvent() {
        if (this.isFramed || this.repairIngredient == ToolMaterials.WOOD.getRepairIngredient()) {
            return SoundEvents.ITEM_SHIELD_BLOCK;
        }
        else {
            if (this.repairIngredient == ModToolMaterials.COPPER.getRepairIngredient()) {
                return ModSoundEvents.COPPER_SHIELD_BLOCK;
            }
            else if (this.repairIngredient == ToolMaterials.GOLD.getRepairIngredient()) {
                return ModSoundEvents.GOLDEN_SHIELD_BLOCK;
            }
            else if (this.repairIngredient == ToolMaterials.IRON.getRepairIngredient()) {
                return ModSoundEvents.IRON_SHIELD_BLOCK;
            }
            else if (this.repairIngredient == ToolMaterials.DIAMOND.getRepairIngredient()) {
                return ModSoundEvents.DIAMOND_SHIELD_BLOCK;
            }
            return ModSoundEvents.NETHERITE_SHIELD_BLOCK;
        }
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        if (this.isFramed && this.material == ToolMaterials.NETHERITE) {
            World world = entity.getWorld();
            world.spawnEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Items.NETHERITE_SCRAP, 4)));
        }
        else {
            super.onItemEntityDestroyed(entity);
        }
    }


}
