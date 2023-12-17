package solipingen.sassot.mixin.client.render.entity;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.item.ModFishingRodItem;
import solipingen.sassot.item.ModToolMaterials;


@Mixin(FishingBobberEntityRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class FishingBobberEntityRendererMixin extends EntityRenderer<FishingBobberEntity> {
    @Shadow @Final private static Identifier TEXTURE;
    @Shadow @Final private static RenderLayer LAYER;
    private static final Identifier COPPER_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/copper_fishing_hook.png");
    private static final Identifier IRON_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/iron_fishing_hook.png");
    private static final Identifier GOLD_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/gold_fishing_hook.png");
    private static final Identifier DIAMOND_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/diamond_fishing_hook.png");
    private static final Identifier NETHERITE_TEXTURE = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/netherite_fishing_hook.png");

    
    protected FishingBobberEntityRendererMixin(Context ctx) {
        super(ctx);
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/FishingBobberEntityRenderer;LAYER:Lnet/minecraft/client/render/RenderLayer;", opcode = Opcodes.GETSTATIC))
    private RenderLayer redirectedRenderLayer(FishingBobberEntity fishingBobberEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        ItemStack fishingRodStack = ItemStack.EMPTY;
        if (fishingBobberEntity.getOwner() instanceof LivingEntity) {
            fishingRodStack = ((LivingEntity)fishingBobberEntity.getOwner()).getMainHandStack();
            if (!(fishingRodStack.getItem() instanceof ModFishingRodItem)) {
                ((LivingEntity)fishingBobberEntity.getOwner()).getOffHandStack();
            }
        }
        if (!fishingRodStack.isEmpty() && fishingRodStack.getItem() instanceof ModFishingRodItem) {
            ToolMaterial material = ((ModFishingRodItem)fishingRodStack.getItem()).getMaterial();
            if (material == ModToolMaterials.COPPER) {
                return RenderLayer.getEntityCutout(COPPER_TEXTURE);
            }
            else if (material == ToolMaterials.IRON) {
                return RenderLayer.getEntityCutout(IRON_TEXTURE);
            }
            else if (material == ToolMaterials.GOLD) {
                return RenderLayer.getEntityCutout(GOLD_TEXTURE);
            }
            else if (material == ToolMaterials.DIAMOND) {
                return RenderLayer.getEntityCutout(DIAMOND_TEXTURE);
            }
            else if (material == ToolMaterials.NETHERITE) {
                return RenderLayer.getEntityCutout(NETHERITE_TEXTURE);
            }
        }
        return LAYER;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean redirectedStackIsOf(ItemStack itemStack, Item originalItem) {
        return itemStack.getItem() instanceof FishingRodItem;
    }

    @ModifyConstant(method = "renderFishingLine", constant = @Constant(intValue = 0))
    private static int modifiedFishingLineColor(int originalInt) {
        return 255;
    }

    @Redirect(method = "getTexture", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/FishingBobberEntityRenderer;TEXTURE:Lnet/minecraft/util/Identifier;", opcode = Opcodes.GETSTATIC))
    private Identifier redirectedTexture(FishingBobberEntity fishingBobberEntity) {
        ItemStack fishingRodStack = ItemStack.EMPTY;
        if (fishingBobberEntity.getOwner() instanceof LivingEntity) {
            fishingRodStack = ((LivingEntity)fishingBobberEntity.getOwner()).getMainHandStack();
            if (!(fishingRodStack.getItem() instanceof ModFishingRodItem)) {
                ((LivingEntity)fishingBobberEntity.getOwner()).getOffHandStack();
            }
        }
        if (!fishingRodStack.isEmpty() && fishingRodStack.getItem() instanceof ModFishingRodItem) {
            ToolMaterial material = ((ModFishingRodItem)fishingRodStack.getItem()).getMaterial();
            if (material == ModToolMaterials.COPPER) {
                return COPPER_TEXTURE;
            }
            else if (material == ToolMaterials.IRON) {
                return IRON_TEXTURE;
            }
            else if (material == ToolMaterials.GOLD) {
                return GOLD_TEXTURE;
            }
            else if (material == ToolMaterials.DIAMOND) {
                return DIAMOND_TEXTURE;
            }
            else if (material == ToolMaterials.NETHERITE) {
                return NETHERITE_TEXTURE;
            }
        }
        return TEXTURE;
    }

    
}
