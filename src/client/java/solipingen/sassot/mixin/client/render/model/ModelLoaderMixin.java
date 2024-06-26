package solipingen.sassot.mixin.client.render.model;

import java.util.List;
import java.util.Map;

import net.minecraft.client.render.model.BlockStatesLoader;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;
import solipingen.sassot.item.BlazearmItem;
import solipingen.sassot.item.SpearItem;


@Mixin(ModelLoader.class)
@Environment(value = EnvType.CLIENT)
public abstract class ModelLoaderMixin {

    @Invoker("loadItemModel")
    public abstract void invokeLoadItemModel(ModelIdentifier modelId);
    

    @Inject(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/item/ItemRenderer;TRIDENT_IN_HAND:Lnet/minecraft/client/util/ModelIdentifier;", opcode = Opcodes.GETSTATIC))
    private void injectedInit(BlockColors blockColors, Profiler profiler, Map<Identifier, JsonUnbakedModel> jsonUnbakedModels, Map<Identifier, List<BlockStatesLoader.SourceTrackedData>> blockStates, CallbackInfo cbi) {
        for (Item item : Registries.ITEM) {
            if (item instanceof SpearItem || item instanceof BlazearmItem) {
                String name = Registries.ITEM.getId(item).getPath();
                this.invokeLoadItemModel(new ModelIdentifier(Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, name + "_in_hand"), "inventory"));
            }
        }
    }


}
