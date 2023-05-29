package solipingen.sassot.mixin.client.render.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;


@Mixin(HeldItemRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class HeldItemRendererMixin {
    @Shadow private ItemStack mainHand;

    
    @ModifyConstant(method = "renderFirstPersonItem", constant = @Constant(floatValue = 0.8f))
    private float modifiedShieldRiptideRender(float originalf, AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (player.isUsingRiptide() && item.getItem() instanceof ShieldItem) {
            return 1.2f;
        }
        return originalf;
    }

    
}
