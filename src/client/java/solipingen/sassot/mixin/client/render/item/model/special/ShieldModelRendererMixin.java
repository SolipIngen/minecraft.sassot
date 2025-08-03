package solipingen.sassot.mixin.client.render.item.model.special;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.item.model.special.ShieldModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.model.ModelBaker;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


@Mixin(ShieldModelRenderer.class)
@Environment(EnvType.CLIENT)
public abstract class ShieldModelRendererMixin implements SpecialModelRenderer<ComponentMap> {
    @Unique private Identifier itemId = Registries.ITEM.getId(Items.SHIELD);


    @Inject(method = "getData(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/component/ComponentMap;", at = @At("HEAD"), cancellable = true)
    private void injecedGetData(ItemStack itemStack, CallbackInfoReturnable<ComponentMap> cbireturn) {
        this.itemId = Registries.ITEM.getId(itemStack.getItem());
    }

    @ModifyVariable(method = "render(Lnet/minecraft/component/ComponentMap;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IIZ)V",
        at = @At("STORE"), ordinal = 0)
    private SpriteIdentifier injectedRender(SpriteIdentifier spriteIdentifier) {
        if (this.itemId.getNamespace().matches(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID)) {
            if (spriteIdentifier == ModelBaker.SHIELD_BASE) {
                return new SpriteIdentifier(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE,
                        Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "entity/shield/" + this.itemId.getPath() + "_base"));
            }
            else {
                return new SpriteIdentifier(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE,
                        Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "entity/shield/" + this.itemId.getPath() + "_base_nopattern"));
            }
        }
        else {
            return spriteIdentifier;
        }
    }

}
