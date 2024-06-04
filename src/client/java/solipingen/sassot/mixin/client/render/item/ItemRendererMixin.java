package solipingen.sassot.mixin.client.render.item;

import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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


    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
        at = @At("HEAD"), index = 8)
    private BakedModel modifiedRenderedModel(BakedModel model, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            boolean renderModeBl = renderMode == ModelTransformationMode.GUI || renderMode == ModelTransformationMode.GROUND || renderMode == ModelTransformationMode.FIXED;
            if ((item instanceof SpearItem || item instanceof BlazearmItem) && renderModeBl) {
                String name = stack.getItem().toString();
                ModelIdentifier modelId = new ModelIdentifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name, "inventory");
                model = this.models.getModelManager().getModel(modelId);
                model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            }
        }
        return model;
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
