package solipingen.sassot.advancement;

import com.mojang.serialization.Codec;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class PlayerSkeweredEntityCriterion extends AbstractCriterion<PlayerSkeweredEntityConditions> {
    static final Identifier ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "player_skewered_entity");


    @Override
    public Codec<PlayerSkeweredEntityConditions> getConditionsCodec() {
        return PlayerSkeweredEntityConditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damage, float dealt, float taken, boolean blocked) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, (conditions) -> conditions.matches(player, lootContext, damage, dealt, taken, blocked));
    }

    
}
