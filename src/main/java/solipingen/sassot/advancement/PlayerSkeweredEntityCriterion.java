package solipingen.sassot.advancement;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import solipingen.sassot.SpearsAxesSwordsShieldsAndOtherTools;


public class PlayerSkeweredEntityCriterion extends AbstractCriterion<PlayerSkeweredEntityConditions> {
    static final Identifier ID = new Identifier(SpearsAxesSwordsShieldsAndOtherTools.MOD_ID, "player_skewered_entity");


    @Override
    public PlayerSkeweredEntityConditions conditionsFromJson(JsonObject jsonObject, Optional<LootContextPredicate> extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        Optional<DamagePredicate> damagePredicate = DamagePredicate.fromJson(jsonObject.get("damage"));
        Optional<LootContextPredicate> lootContextPredicate2 = EntityPredicate.contextPredicateFromJson(jsonObject, "entity", advancementEntityPredicateDeserializer);
        return new PlayerSkeweredEntityConditions(extended, damagePredicate, lootContextPredicate2);
    }

    public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damage, float dealt, float taken, boolean blocked) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, (conditions) -> conditions.matches(player, lootContext, damage, dealt, taken, blocked));
    }
    
}
