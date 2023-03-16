package solipingen.sassot.mixin.server.network;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow @Final public static double MAX_BREAK_SQUARED_DISTANCE;
    @Shadow public ServerPlayerEntity player;
    

    @Redirect(method = "onPlayerInteractEntity", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D", opcode = Opcodes.GETSTATIC))
    private double redirectedMaxBreakSquaredDistance() {
        double originalMaxBreakDistance = Math.sqrt(MAX_BREAK_SQUARED_DISTANCE);
        ServerPlayerEntity serverPlayerEntity = this.player;
        if (serverPlayerEntity != null) {
            ItemStack mainHandStack = serverPlayerEntity.getMainHandStack();
            if (mainHandStack.getItem() instanceof SwordItem) {
                return MathHelper.square(originalMaxBreakDistance + 1.0);
            }
            else if (mainHandStack.getItem() instanceof SpearItem || mainHandStack.getItem() instanceof BlazearmItem || mainHandStack.getItem() instanceof TridentItem) {
                int thrustLevel = EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, mainHandStack);
                double thrustAddition = (serverPlayerEntity.isOnGround() && !serverPlayerEntity.isSprinting() && !(mainHandStack.getItem() instanceof BlazearmItem)) ? thrustLevel/3.0 : 0.0;
                return MathHelper.square(originalMaxBreakDistance + 2.0 + thrustAddition);
            }
        }
        return MAX_BREAK_SQUARED_DISTANCE;
    }
    
}
