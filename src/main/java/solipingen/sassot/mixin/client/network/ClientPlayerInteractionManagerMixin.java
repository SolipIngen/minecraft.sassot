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
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameMode;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(ClientPlayerInteractionManager.class)
@Environment(value = EnvType.CLIENT)
public abstract class ClientPlayerInteractionManagerMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private GameMode gameMode;

    
    @ModifyConstant(method = "getReachDistance", constant = @Constant(floatValue = 5.0f))
    private float modifiedCreativeGetReachDistance(float originalFloat) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            return originalFloat + 0.5f;
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            return originalFloat + 1.0f;
        }
        return originalFloat;
    }

    @ModifyConstant(method = "getReachDistance", constant = @Constant(floatValue = 4.5f))
    private float modifiedGetReachDistance(float originalFloat) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            return originalFloat + 0.5f;
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            return originalFloat + 1.0f;
        }
        return originalFloat;
    }

}
