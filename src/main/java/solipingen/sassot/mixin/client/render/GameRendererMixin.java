package solipingen.sassot.mixin.client.render;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(GameRenderer.class)
@Environment(value=EnvType.CLIENT)
public abstract class GameRendererMixin implements AutoCloseable {
    @Shadow @Final MinecraftClient client;
    

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3.0))
    private double modifiedAttackReachConstant(double originalReach) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
        if (mainHandStack.getItem() instanceof SwordItem) {
            return originalReach + 1.0;
        }
        else if (mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof TridentItem) {
            int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, mainHandStack);
            double thrustAddition = (clientPlayerEntity.isOnGround() && !clientPlayerEntity.isSprinting() && !(mainHandStack.getItem() instanceof BlazearmItem)) ? thrustLevel/3.0 : 0.0;
            return originalReach + 2.0 + thrustAddition;
        }
        return originalReach;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 6.0))
    private double modifiedRangeConstant(double originalRange) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
        if (mainHandStack.getItem() instanceof SwordItem) {
            return originalRange + 1.0;
        }
        else if (mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof TridentItem) {
            int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, mainHandStack);
            double thrustAddition = (clientPlayerEntity.isOnGround() && !clientPlayerEntity.isSprinting() && !(mainHandStack.getItem() instanceof BlazearmItem)) ? thrustLevel/3.0 : 0.0;
            return originalRange + 2.0 + thrustAddition;
        }
        return originalRange;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9.0))
    private double modifiedAttackReachSquaredConstant(double originalReachSquared) {
        double originalReach = Math.sqrt(originalReachSquared);
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        if (clientPlayerEntity != null) {
            ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
            if (mainHandStack.getItem() instanceof SwordItem) {
                return MathHelper.square(originalReach + 1.0);
            }
            else if (mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof TridentItem) {
                int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, mainHandStack);
                double thrustAddition = (clientPlayerEntity.isOnGround() && !clientPlayerEntity.isSprinting() && !(mainHandStack.getItem() instanceof BlazearmItem)) ? thrustLevel/3.0 : 0.0;
                return MathHelper.square(originalReach + 2.0 + thrustAddition);
            }
        }
        return originalReachSquared;
    }


}
