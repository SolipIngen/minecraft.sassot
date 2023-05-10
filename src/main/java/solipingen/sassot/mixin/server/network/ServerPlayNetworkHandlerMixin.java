package solipingen.sassot.mixin.server.network;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.registry.tag.ModItemTags;


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
            if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
                return MathHelper.square(originalMaxBreakDistance + 0.5);
            }
            else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
                return MathHelper.square(originalMaxBreakDistance + 1.0);
            }
        }
        return MAX_BREAK_SQUARED_DISTANCE;
    }
    
}
