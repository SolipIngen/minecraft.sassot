package solipingen.sassot.mixin.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.item.SpearItem;


@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends ToolItem {
    @Unique private static final int ATTACK_DAMAGE = 3;


    public SwordItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @ModifyConstant(method = "createToolComponent", constant = @Constant(floatValue = 15.0f))
    private static float modifiedCobwebMiningSpeedMultiplier(float originalf) {
        return 25.0f;
    }

    @ModifyConstant(method = "createToolComponent", constant = @Constant(floatValue = 1.5f))
    private static float modifiedPlantMiningSpeedMultiplier(float originalf) {
        return 3.0f;
    }

    @ModifyConstant(method = "createToolComponent", constant = @Constant(intValue = 2))
    private static int modifiedDamagerPerBlock(int originalI) {
        return 1;
    }

    @Inject(method = "createAttributeModifiers", at = @At("HEAD"), cancellable = true)
    private static void injectedAttributeModifiers(ToolMaterial material, int baseAttackDamage, float attackSpeed, CallbackInfoReturnable<AttributeModifiersComponent> cbireturn) {
        float modAttackSpeed = SwordItemMixin.getModAttackSpeed(material, attackSpeed);
        cbireturn.setReturnValue(AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, ATTACK_DAMAGE + material.getAttackDamage(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, modAttackSpeed, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE,
                        new EntityAttributeModifier(SpearItem.RANGE_MODIFIER_ID, 0.5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                        new EntityAttributeModifier(SpearItem.ATTACK_RANGE_MODIFIER_ID, 0.5, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND).build());
    }

    @Unique
    private static float getModAttackSpeed(ToolMaterial material, float attackSpeed) {
        float modAttackSpeed = attackSpeed;
        if (material == ToolMaterials.WOOD) {
            modAttackSpeed = -1.8f;
        }
        else if (material == ToolMaterials.STONE) {
            modAttackSpeed = -2.4f;
        }
        else if (material == ToolMaterials.GOLD) {
            modAttackSpeed = -2.3f;
        }
        else if (material == ToolMaterials.IRON) {
            modAttackSpeed = -2.2f;
        }
        else if (material == ToolMaterials.DIAMOND) {
            modAttackSpeed = -2.0f;
        }
        else if (material == ToolMaterials.NETHERITE) {
            modAttackSpeed = -1.9f;
        }
        return modAttackSpeed;
    }


}
