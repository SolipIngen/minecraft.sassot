package solipingen.sassot.mixin.client.render.item;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.world.World;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(ItemRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class ItemRendererMixin implements SynchronousResourceReloader {
    @Shadow @Final private ItemModels models;

    @Invoker("renderBakedItemModel")
    public abstract void invokeRenderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices);


    @Inject(method = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", 
        at = @At("HEAD"), cancellable = true)
    private void injectedRenderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo cbi) {
        if (model.isSideLit() && renderMode == ModelTransformationMode.GUI) {
            DiffuseLighting.enableGuiDepthLighting();
        }
        if ((stack.getItem() instanceof SpearItem || stack.getItem() instanceof BlazearmItem) && (renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED)) {
            matrices.push();
            String name = stack.getItem().toString();
            ModelIdentifier modelId = new ModelIdentifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name, "inventory");
            model = this.models.getModelManager().getModel(modelId);
            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5f, -0.5f, -0.5f);
            RenderLayer renderLayer = RenderLayers.getItemLayer(stack, true);
            VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());
            if (renderMode == ModelTransformationMode.GUI) {
                DiffuseLighting.disableGuiDepthLighting();
            }
            this.invokeRenderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
            matrices.pop();
            cbi.cancel();
        }
    }

    @ModifyVariable(method = "getModel", at = @At("STORE"), ordinal = 0)
    private BakedModel modifiedBakedModel(BakedModel originalBakedModel, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed) {
        if (stack.getItem() instanceof SpearItem || stack.getItem() instanceof BlazearmItem) {
            String name = stack.getItem().toString();
            ModelIdentifier inHandModelId = new ModelIdentifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name + "_in_hand", "inventory");
            return this.models.getModelManager().getModel(inHandModelId);
        }
        return originalBakedModel;
    }


}
