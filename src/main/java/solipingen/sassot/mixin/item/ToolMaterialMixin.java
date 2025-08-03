package solipingen.sassot.mixin.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.WeaponComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.item.SpearItem;

import java.util.List;


@Mixin(ToolMaterial.class)
public abstract class ToolMaterialMixin {
    @Shadow @Final private float speed;
    @Shadow @Final private int enchantmentValue;
    @Shadow @Final private TagKey<Item> repairItems;
    @Unique private static final int SWORD_ATTACK_DAMAGE = 3;

    @Invoker("applyBaseSettings")
    public abstract Item.Settings invokeApplyBaseSettings(Item.Settings settings);


    @Inject(method = "applyToolSettings", at = @At("HEAD"), cancellable = true)
    private void injectedApplyToolSettings(Item.Settings settings, TagKey<Block> effectiveBlocks, float attackDamage, float attackSpeed, float disableBlockingForSeconds, CallbackInfoReturnable<Item.Settings> cbireturn) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        int modDurability = ToolMaterialMixin.getModDurability(((ToolMaterial)(Object)this));
        float modSpeed = ((ToolMaterial)(Object)this).speed();
//        if (((ToolMaterial)(Object)this) == ToolMaterial.GOLD) {
//            modSpeed += 1.0f;
//        }
//        else if (((ToolMaterial)(Object)this) == ToolMaterial.IRON) {
//            modSpeed += 2.0f;
//        }
//        else if (((ToolMaterial)(Object)this) == ToolMaterial.DIAMOND || ((ToolMaterial)(Object)this) == ToolMaterial.NETHERITE) {
//            modSpeed += 4.0f;
//        }
        cbireturn.setReturnValue(settings.maxDamage(modDurability).repairable(this.repairItems).enchantable(this.enchantmentValue)
                .component(DataComponentTypes.TOOL,
                        new ToolComponent(List.of(ToolComponent.Rule.ofNeverDropping(registryEntryLookup.getOrThrow(((ToolMaterial)(Object)this).incorrectBlocksForDrops())),
                                ToolComponent.Rule.ofAlwaysDropping(registryEntryLookup.getOrThrow(effectiveBlocks), modSpeed)), 1.0F, 1, true))
                .attributeModifiers(this.createToolAttributeModifiers(attackDamage, attackSpeed))
                .component(DataComponentTypes.WEAPON, new WeaponComponent(1, disableBlockingForSeconds)));
    }

    @Inject(method = "applySwordSettings", at = @At("HEAD"), cancellable = true)
    private void injectedSwordSettings(Item.Settings settings, float attackDamage, float attackSpeed, CallbackInfoReturnable<Item.Settings> cbireturn) {
        RegistryEntryLookup<Block> registryEntryLookup = Registries.createEntryLookup(Registries.BLOCK);
        int modDurability = ToolMaterialMixin.getModDurability(((ToolMaterial)(Object)this));
        cbireturn.setReturnValue(settings.maxDamage(modDurability).repairable(this.repairItems).enchantable(this.enchantmentValue)
                .component(DataComponentTypes.TOOL,
                        new ToolComponent(List.of(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(new RegistryEntry[]{Blocks.COBWEB.getRegistryEntry()}), 25.0f),
                                ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE), ToolComponent.Rule.of(registryEntryLookup.getOrThrow(BlockTags.SWORD_EFFICIENT), 3.0f)), 1.0f, 1, false))
                .attributeModifiers(this.createSwordAttributeModifiers(attackDamage, attackSpeed)).component(DataComponentTypes.WEAPON, new WeaponComponent(1)));
    }

    @Unique
    private AttributeModifiersComponent createToolAttributeModifiers(float attackDamage, float attackSpeed) {
        float modAttackDamage = ToolMaterialMixin.getModAttackDamage((ToolMaterial)(Object)this, attackDamage);
        float modAttackSpeed = ToolMaterialMixin.getModAttackSpeed((ToolMaterial)(Object)this, attackSpeed);
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, modAttackDamage, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, modAttackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    @Unique
    private AttributeModifiersComponent createSwordAttributeModifiers(float attackDamage, float attackSpeed) {
        float modAttackDamage = ToolMaterialMixin.getModAttackDamage((ToolMaterial)(Object)this, attackDamage);
        float modAttackSpeed = ToolMaterialMixin.getModAttackSpeed((ToolMaterial)(Object)this, attackSpeed);
        return AttributeModifiersComponent.builder().add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, modAttackDamage, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, modAttackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.BLOCK_INTERACTION_RANGE,
                        new EntityAttributeModifier(SpearItem.RANGE_MODIFIER_ID, 0.5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ENTITY_INTERACTION_RANGE,
                        new EntityAttributeModifier(SpearItem.ATTACK_RANGE_MODIFIER_ID, 0.5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build();
    }

    @Inject(method = "durability", at = @At("HEAD"), cancellable = true)
    private void injectedDurability(CallbackInfoReturnable<Integer> cbireturn) {
        if (((ToolMaterial)(Object)this) == ToolMaterial.WOOD) {
            cbireturn.setReturnValue(42);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.STONE) {
            cbireturn.setReturnValue(148);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.GOLD) {
            cbireturn.setReturnValue(123);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.IRON) {
            cbireturn.setReturnValue(2*250);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.DIAMOND) {
            cbireturn.setReturnValue(MathHelper.ceil(1.2f*1561));
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.NETHERITE) {
            cbireturn.setReturnValue(MathHelper.ceil(1.2f*2031));
        }
    }

    @Inject(method = "speed", at = @At("HEAD"), cancellable = true)
    private void injectedSpeed(CallbackInfoReturnable<Float> cbireturn) {
        if (((ToolMaterial)(Object)this) == ToolMaterial.GOLD) {
            cbireturn.setReturnValue(this.speed + 1.0f);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.IRON) {
            cbireturn.setReturnValue(this.speed + 2.0f);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.DIAMOND || ((ToolMaterial)(Object)this) == ToolMaterial.NETHERITE) {
            cbireturn.setReturnValue(this.speed + 4.0f);
        }
    }

    @Inject(method = "attackDamageBonus", at = @At("HEAD"), cancellable = true)
    private void injectedAttackDamageBonus(CallbackInfoReturnable<Float> cbireturn) {
        if (((ToolMaterial)(Object)this) == ToolMaterial.GOLD) {
            cbireturn.setReturnValue(2.0f);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.IRON) {
            cbireturn.setReturnValue(4.0f);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.DIAMOND) {
            cbireturn.setReturnValue(6.0f);
        }
        else if (((ToolMaterial)(Object)this) == ToolMaterial.NETHERITE) {
            cbireturn.setReturnValue(7.0f);
        }
    }

    @Unique
    private static int getModDurability(ToolMaterial material) {
        if (material == ToolMaterial.WOOD) {
            return 42;
        }
        else if (material == ToolMaterial.STONE) {
            return 148;
        }
        else if (material == ToolMaterial.GOLD) {
            return 123;
        }
        else if (material == ToolMaterial.IRON) {
            return 2*material.durability();
        }
        else if (material == ToolMaterial.DIAMOND || material == ToolMaterial.NETHERITE) {
            return MathHelper.ceil(1.2f*material.durability());
        }
        return material.durability();
    }

    @Unique
    private static float getModAttackDamage(ToolMaterial material, float attackDamage) {
        if (material == ToolMaterial.GOLD) {
            return attackDamage + 2.0f;
        }
        else if (material == ToolMaterial.IRON) {
            return attackDamage + 4.0f;
        }
        else if (material == ToolMaterial.DIAMOND) {
            return attackDamage + 6.0f;
        }
        else if (material == ToolMaterial.NETHERITE) {
            return attackDamage + 7.0f;
        }
        return attackDamage + material.attackDamageBonus();
    }

    @Unique
    private static float getModAttackSpeed(ToolMaterial material, float attackSpeed) {
        float modAttackSpeed = attackSpeed;
        if (material == ToolMaterial.WOOD) {
            modAttackSpeed = -1.8f;
        }
        else if (material == ToolMaterial.STONE) {
            modAttackSpeed = -2.4f;
        }
        else if (material == ToolMaterial.GOLD) {
            modAttackSpeed = -2.3f;
        }
        else if (material == ToolMaterial.IRON) {
            modAttackSpeed = -2.2f;
        }
        else if (material == ToolMaterial.DIAMOND) {
            modAttackSpeed = -2.0f;
        }
        else if (material == ToolMaterial.NETHERITE) {
            modAttackSpeed = -1.9f;
        }
        return modAttackSpeed;
    }

}
