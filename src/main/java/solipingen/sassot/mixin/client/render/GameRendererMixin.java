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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.registry.tag.ModItemTags;


@Mixin(GameRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class GameRendererMixin implements AutoCloseable {
    @Shadow @Final MinecraftClient client;
    

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 3.0))
    private double modifiedAttackReachConstant(double originalReach) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            return originalReach + 0.5;
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            return originalReach + 1.0;
        }
        return originalReach;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 6.0))
    private double modifiedRangeConstant(double originalRange) {
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
        if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
            return originalRange + 0.5;
        }
        else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
            return originalRange + 1.0;
        }
        return originalRange;
    }

    @ModifyConstant(method = "updateTargetedEntity", constant = @Constant(doubleValue = 9.0))
    private double modifiedAttackReachSquaredConstant(double originalReachSquared) {
        double originalReach = Math.sqrt(originalReachSquared);
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        if (clientPlayerEntity != null) {
            ItemStack mainHandStack = clientPlayerEntity.getMainHandStack();
            if (mainHandStack.isIn(ModItemTags.SWEEPING_WEAPONS)) {
                return MathHelper.square(originalReach + 0.5);
            }
            else if (mainHandStack.isIn(ModItemTags.THRUSTING_WEAPONS) || mainHandStack.isOf(ModItems.BLAZEARM)) {
                return MathHelper.square(originalReach + 1.0);
            }
        }
        return originalReachSquared;
    }


}
