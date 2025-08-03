package solipingen.sassot.advancement;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public record PlayerSkeweredEntityConditions(Optional<LootContextPredicate> player, Optional<DamagePredicate> damage, Optional<LootContextPredicate> entity) implements AbstractCriterion.Conditions {
    static final Identifier ID = PlayerSkeweredEntityCriterion.ID;
    public static final Codec<PlayerSkeweredEntityConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(PlayerSkeweredEntityConditions::player),
            DamagePredicate.CODEC.optionalFieldOf("damage").forGetter(PlayerSkeweredEntityConditions::damage),
            EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("entity").forGetter(PlayerSkeweredEntityConditions::entity))
        .apply(instance, PlayerSkeweredEntityConditions::new));


    public static AdvancementCriterion<PlayerSkeweredEntityConditions> create() {
        return ModCriteria.PLAYER_SKEWERED_ENTITY.create(new PlayerSkeweredEntityConditions(Optional.empty(), Optional.empty(), Optional.empty()));
    }

    public static AdvancementCriterion<PlayerSkeweredEntityConditions> createDamage(Optional<DamagePredicate> damagePredicate) {
        return ModCriteria.PLAYER_SKEWERED_ENTITY.create(new PlayerSkeweredEntityConditions(Optional.empty(), damagePredicate, Optional.empty()));
    }

    public static AdvancementCriterion<PlayerSkeweredEntityConditions> create(DamagePredicate.Builder damagePredicateBuilder) {
        return ModCriteria.PLAYER_SKEWERED_ENTITY.create(new PlayerSkeweredEntityConditions(Optional.empty(), Optional.of(damagePredicateBuilder.build()), Optional.empty()));
    }

    public static AdvancementCriterion<PlayerSkeweredEntityConditions> create(Optional<EntityPredicate> hurtEntityPredicate) {
        return ModCriteria.PLAYER_SKEWERED_ENTITY.create(new PlayerSkeweredEntityConditions(Optional.empty(), Optional.empty(), EntityPredicate.contextPredicateFromEntityPredicate(hurtEntityPredicate)));
    }

    public static AdvancementCriterion<PlayerSkeweredEntityConditions> create(Optional<DamagePredicate> damagePredicate, Optional<EntityPredicate> hurtEntityPredicate) {
        return ModCriteria.PLAYER_SKEWERED_ENTITY.create(new PlayerSkeweredEntityConditions(Optional.empty(), damagePredicate, EntityPredicate.contextPredicateFromEntityPredicate(hurtEntityPredicate)));
    }

    public static AdvancementCriterion<PlayerSkeweredEntityConditions> create(DamagePredicate.Builder damagePredicateBuilder, Optional<EntityPredicate> hurtEntityPredicate) {
        return ModCriteria.PLAYER_SKEWERED_ENTITY.create(new PlayerSkeweredEntityConditions(Optional.empty(), Optional.of(damagePredicateBuilder.build()), EntityPredicate.contextPredicateFromEntityPredicate(hurtEntityPredicate)));
    }

    public boolean matches(ServerPlayerEntity player, LootContext entityContext, DamageSource source, float dealt, float taken, boolean blocked) {
        if (this.damage.isPresent() && !this.damage.get().test(player, source, dealt, taken, blocked)) {
            return false;
        }
        return !this.entity.isPresent() || this.entity.get().test(entityContext);
    }


}
