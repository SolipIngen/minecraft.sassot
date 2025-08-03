package solipingen.sassot.advancement;

import com.mojang.serialization.Codec;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class ThrowSpearCriterion extends AbstractCriterion<ThrowSpearConditions> {
    static final Identifier ID = Identifier.of(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "throw_spear");

    
    @Override
    public Codec<ThrowSpearConditions> getConditionsCodec() {
        return ThrowSpearConditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, (conditions) -> conditions.matches(stack));
    }


    
}
