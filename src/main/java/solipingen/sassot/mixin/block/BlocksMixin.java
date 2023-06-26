package solipingen.sassot.mixin.block;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CalibratedSculkSensorBlock;
import net.minecraft.block.CobwebBlock;
import net.minecraft.block.SculkBlock;
import net.minecraft.block.SculkCatalystBlock;
import net.minecraft.block.SculkSensorBlock;
import net.minecraft.block.SculkShriekerBlock;
import net.minecraft.block.SculkVeinBlock;
import net.minecraft.block.enums.SculkSensorPhase;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;


@Mixin(Blocks.class)
public abstract class BlocksMixin {
    

    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void injectedRegister(String name, Block entry, CallbackInfoReturnable<Block> cbireturn) {
        if (entry instanceof CobwebBlock) {
            Registry.register(Registries.BLOCK, name, entry);
            int rawId = Registries.BLOCK.getRawId(entry);
            Block newEntry = (Block)new CobwebBlock(AbstractBlock.Settings.copy(entry).hardness(1.0f).resistance(0.1f));
            cbireturn.setReturnValue(Registry.register(Registries.BLOCK, rawId, name, newEntry));
        }
        else if (entry instanceof SculkBlock) {
            Registry.register(Registries.BLOCK, name, entry);
            int rawId = Registries.BLOCK.getRawId(entry);
            Block newEntry = (Block)new SculkBlock(AbstractBlock.Settings.copy(entry).luminance(state -> 2));
            cbireturn.setReturnValue(Registry.register(Registries.BLOCK, rawId, name, newEntry));
        }
        else if (entry instanceof SculkVeinBlock) {
            Registry.register(Registries.BLOCK, name, entry);
            int rawId = Registries.BLOCK.getRawId(entry);
            Block newEntry = (Block)new SculkVeinBlock(AbstractBlock.Settings.copy(entry).replaceable().pistonBehavior(PistonBehavior.DESTROY).noCollision().strength(0.2f).luminance(state -> 1));
            cbireturn.setReturnValue(Registry.register(Registries.BLOCK, rawId, name, newEntry));
        }
        else if (entry instanceof SculkSensorBlock && !(entry instanceof CalibratedSculkSensorBlock)) {
            Registry.register(Registries.BLOCK, name, entry);
            int rawId = Registries.BLOCK.getRawId(entry);
            Block newEntry = (Block)new SculkSensorBlock(AbstractBlock.Settings.copy(entry).luminance(state -> state.get(SculkSensorBlock.SCULK_SENSOR_PHASE) == SculkSensorPhase.ACTIVE ? 3 : 2));
            cbireturn.setReturnValue(Registry.register(Registries.BLOCK, rawId, name, newEntry));
        }
        else if (entry instanceof CalibratedSculkSensorBlock) {
            Registry.register(Registries.BLOCK, name, entry);
            int rawId = Registries.BLOCK.getRawId(entry);
            Block newEntry = (Block)new CalibratedSculkSensorBlock(AbstractBlock.Settings.copy(entry).luminance(state -> state.get(SculkSensorBlock.SCULK_SENSOR_PHASE) == SculkSensorPhase.ACTIVE ? 3 : 2));
            cbireturn.setReturnValue(Registry.register(Registries.BLOCK, rawId, name, newEntry));
        }
        else if (entry instanceof SculkCatalystBlock) {
            Registry.register(Registries.BLOCK, name, entry);
            int rawId = Registries.BLOCK.getRawId(entry);
            Block newEntry = (Block)new SculkCatalystBlock(AbstractBlock.Settings.copy(entry).luminance(state -> state.get(SculkCatalystBlock.BLOOM) ? 10 : 6));
            cbireturn.setReturnValue(Registry.register(Registries.BLOCK, rawId, name, newEntry));
        }
        else if (entry instanceof SculkShriekerBlock) {
            Registry.register(Registries.BLOCK, name, entry);
            int rawId = Registries.BLOCK.getRawId(entry);
            Block newEntry = (Block)new SculkShriekerBlock(AbstractBlock.Settings.copy(entry).luminance(state -> state.get(SculkShriekerBlock.SHRIEKING) ? 4 : 2));
            cbireturn.setReturnValue(Registry.register(Registries.BLOCK, rawId, name, newEntry));
        }
    }


}
