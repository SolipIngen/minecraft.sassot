package solipingen.sassot.mixin.client.render.entity.feature;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.TridentRiptideFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.util.interfaces.mixin.entity.LivingEntityInterface;


@Mixin(TridentRiptideFeatureRenderer.class)
@Environment(value = EnvType.CLIENT)
public abstract class TridentRiptideFeatureRendererMixin<T extends LivingEntity> {
    @Shadow @Final private static Identifier TEXTURE;


    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/feature/TridentRiptideFeatureRenderer;TEXTURE:Lnet/minecraft/util/Identifier;", opcode = Opcodes.GETSTATIC))
    private Identifier redirectedTEXTURE(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
        LivingEntityInterface iLivingEntity = (LivingEntityInterface)livingEntity;
        if (iLivingEntity.getIsUsingFlare() && !iLivingEntity.getIsUsingWhirlwind() && iLivingEntity.getRiptideTicks() > 0) {
            return new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/blazearm_flare.png");
        }
        else if (!(iLivingEntity.getIsUsingFlare() || iLivingEntity.getIsUsingWhirlwind()) && iLivingEntity.getRiptideTicks() > 0) {
            return new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/trident_riptide.png");
        }
        else if (iLivingEntity.getIsUsingWhirlwind() && !iLivingEntity.getIsUsingFlare() && iLivingEntity.getRiptideTicks() > 0) {
            return new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "textures/entity/spear_whirlwind.png");
        }
        return TEXTURE;
    }


}
