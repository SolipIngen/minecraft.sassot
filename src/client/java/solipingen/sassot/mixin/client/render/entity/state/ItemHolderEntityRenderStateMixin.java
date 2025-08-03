package solipingen.sassot.mixin.client.render.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.Arm;
import net.minecraft.village.VillagerDataContainer;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.village.ModVillagerProfessions;


@Mixin(ItemHolderEntityRenderState.class)
@Environment(EnvType.CLIENT)
public abstract class ItemHolderEntityRenderStateMixin extends LivingEntityRenderState {


    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private static void injectedUpdate(LivingEntity entity, ItemHolderEntityRenderState state, ItemModelManager itemModelManager, CallbackInfo cbi) {
        if (entity instanceof VillagerDataContainer villagerDataContainer) {
            VillagerProfession profession = villagerDataContainer.getVillagerData().profession().value();
            if (profession == ModVillagerProfessions.SWORDSMAN || profession == ModVillagerProfessions.SPEARMAN) {
                if (entity.getMainArm() == Arm.LEFT) {
                    itemModelManager.updateForLivingEntity(state.itemRenderState, entity.getMainHandStack(), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
                }
                else {
                    itemModelManager.updateForLivingEntity(state.itemRenderState, entity.getMainHandStack(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
                }
                cbi.cancel();
            }
        }
    }

}
