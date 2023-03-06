package solipingen.sassot.mixin.client.network;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.world.GameMode;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(ClientPlayerInteractionManager.class)
@Environment(value=EnvType.CLIENT)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private GameMode gameMode;

    
    @ModifyConstant(method = "getReachDistance", constant = @Constant(floatValue = 5.0f))
    private float modifiedCreativeGetReachDistance(float originalFloat) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
        if (mainHandStack.getItem() instanceof SwordItem) {
            return originalFloat + 1.0f;
        }
        else if (mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof TridentItem) {
            int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, mainHandStack);
            float thrustAddition = (clientPlayerEntity.isOnGround() && !clientPlayerEntity.isSprinting() && !(mainHandStack.getItem() instanceof BlazearmItem)) ? thrustLevel/3.0f : 0.0f;
            return originalFloat + 2.0f + thrustAddition;
        }
        return originalFloat;
    }

    @ModifyConstant(method = "getReachDistance", constant = @Constant(floatValue = 4.5f))
    private float modifiedGetReachDistance(float originalFloat) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
        if (mainHandStack.getItem() instanceof SwordItem) {
            return originalFloat + 1.0f;
        }
        else if (mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof TridentItem) {
            int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, mainHandStack);
            float thrustAddition = (clientPlayerEntity.isOnGround() && !clientPlayerEntity.isSprinting() && !(mainHandStack.getItem() instanceof BlazearmItem)) ? thrustLevel/3.0f : 0.0f;
            return originalFloat + 2.0f + thrustAddition;
        }
        return originalFloat;
    }

}
