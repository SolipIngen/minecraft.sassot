package solipingen.sassot.mixin.item;

import java.util.function.Consumer;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import solipingen.sassot.item.SpearItem;


@Mixin(SwordItem.class)
public abstract class SwordItemMixin extends ToolItem implements Vanishable {
    @Shadow @Final private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;


    public SwordItemMixin(ToolMaterial material, Settings settings) {
        super(material, settings);
    }

    @ModifyConstant(method = "getMiningSpeedMultiplier", constant = @Constant(floatValue = 15.0f))
    private float modifiedCobwebMiningSpeedMultiplier(float originalf) {
        return 25.0f;
    }

    @ModifyConstant(method = "getMiningSpeedMultiplier", constant = @Constant(floatValue = 1.5f))
    private float modifiedPlantMiningSpeedMultiplier(float originalf) {
        return 3.0f;
    }

    @Redirect(method = "postMine", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"))
    private void redirectedStackDamage(ItemStack originalItemStack, int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback, ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (this.getMiningSpeedMultiplier(stack, state) > 1.0f) {
            stack.damage(1, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
    }

    @Inject(method = "getAttributeModifiers", at = @At(value = "FIELD", target = "Lnet/minecraft/item/SwordItem;attributeModifiers:Lcom/google/common/collect/Multimap;", opcode = Opcodes.GETFIELD), cancellable = true)
    private void injectedGetAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cbireturn) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        this.attributeModifiers.forEach((attribute, modifier) -> builder.put(attribute, modifier));
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(SpearItem.REACH_MODIFIER_ID, "Weapon modifier", 0.5, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(SpearItem.ATTACK_RANGE_MODIFIER_ID, "Weapon modifier", 0.5, EntityAttributeModifier.Operation.ADDITION));
        cbireturn.setReturnValue(builder.build());
    }

    
}
