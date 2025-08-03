package solipingen.sassot.mixin.client.render.entity;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.state.FishingBobberEntityState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.*;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.item.ModFishingRodItem;
import solipingen.sassot.item.ModToolMaterials;


@Mixin(FishingBobberEntityRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class FishingBobberEntityRendererMixin extends EntityRenderer<FishingBobberEntity, FishingBobberEntityState> {
    @Shadow @Final private static Identifier TEXTURE;
    @Shadow @Final private static RenderLayer LAYER;
    private static final Identifier COPPER_TEXTURE = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/copper_fishing_hook.png");
    private static final Identifier IRON_TEXTURE = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/iron_fishing_hook.png");
    private static final Identifier GOLD_TEXTURE = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/gold_fishing_hook.png");
    private static final Identifier DIAMOND_TEXTURE = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/diamond_fishing_hook.png");
    private static final Identifier NETHERITE_TEXTURE = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/netherite_fishing_hook.png");
    @Unique private Identifier texture = TEXTURE;

    @Invoker("vertex")
    public static void invokeVertex(VertexConsumer buffer, MatrixStack.Entry matrix, int light, float x, int y, int u, int v) {
        throw new AssertionError();
    }

    @Invoker("percentage")
    public static float invokePercentage(int value, int denominator) {
        throw new AssertionError();
    }

    protected FishingBobberEntityRendererMixin(Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/client/render/entity/state/FishingBobberEntityState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"), cancellable = true)
    private void injectedRender(FishingBobberEntityState fishingBobberEntityState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo cbi) {
        matrixStack.push();
        matrixStack.push();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.multiply(this.dispatcher.getRotation());
        MatrixStack.Entry entry = matrixStack.peek();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.texture));
        invokeVertex(vertexConsumer, entry, i, 0.0F, 0, 0, 1);
        invokeVertex(vertexConsumer, entry, i, 1.0F, 0, 1, 1);
        invokeVertex(vertexConsumer, entry, i, 1.0F, 1, 1, 0);
        invokeVertex(vertexConsumer, entry, i, 0.0F, 1, 0, 0);
        matrixStack.pop();
        float f = (float)fishingBobberEntityState.pos.x;
        float g = (float)fishingBobberEntityState.pos.y;
        float h = (float)fishingBobberEntityState.pos.z;
        VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getLineStrip());
        MatrixStack.Entry entry2 = matrixStack.peek();
        for(int k = 0; k <= 16; ++k) {
            renderFishingLine(f, g, h, vertexConsumer2, entry2, invokePercentage(k, 16), invokePercentage(k + 1, 16));
        }
        matrixStack.pop();
        super.render(fishingBobberEntityState, matrixStack, vertexConsumerProvider, i);
    }

    @ModifyConstant(method = "renderFishingLine", constant = @Constant(intValue = -16777216))
    private static int modifiedFishingLineColor(int originalInt) {
        return -1;
    }

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/projectile/FishingBobberEntity;Lnet/minecraft/client/render/entity/state/FishingBobberEntityState;F)V",
            at = @At("HEAD"))
    private void injectedUpdateRenderState(FishingBobberEntity fishingBobberEntity, FishingBobberEntityState fishingBobberEntityState, float f, CallbackInfo cbi) {
        if (fishingBobberEntity.getOwner() instanceof LivingEntity) {
            ItemStack fishingRodStack = ((LivingEntity)fishingBobberEntity.getOwner()).getMainHandStack();
            if (!(fishingRodStack.getItem() instanceof ModFishingRodItem)) {
                fishingRodStack = ((LivingEntity)fishingBobberEntity.getOwner()).getOffHandStack();
            }
            if (!fishingRodStack.isEmpty() && fishingRodStack.getItem() instanceof ModFishingRodItem modFishingRodItem) {
                ToolMaterial material = modFishingRodItem.getMaterial();
                if (material == ModToolMaterials.COPPER) {
                    this.texture = COPPER_TEXTURE;
                }
                else if (material == ToolMaterial.IRON) {
                    this.texture = IRON_TEXTURE;
                }
                else if (material == ToolMaterial.GOLD) {
                    this.texture = GOLD_TEXTURE;
                }
                else if (material == ToolMaterial.DIAMOND) {
                    this.texture = DIAMOND_TEXTURE;
                }
                else if (material == ToolMaterial.NETHERITE) {
                    this.texture = NETHERITE_TEXTURE;
                }
            }
        }
    }

    @Unique
    private static void renderFishingLine(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd) {
        float f = x * segmentStart;
        float g = y * (segmentStart * segmentStart + segmentStart) * 0.5F + 0.25F;
        float h = z * segmentStart;
        float i = x * segmentEnd - f;
        float j = y * (segmentEnd * segmentEnd + segmentEnd) * 0.5F + 0.25F - g;
        float k = z * segmentEnd - h;
        float l = MathHelper.sqrt(i * i + j * j + k * k);
        i /= l;
        j /= l;
        k /= l;
        buffer.vertex(matrices, f, g, h).color(-16777216).normal(matrices, i, j, k);
    }

    
}
