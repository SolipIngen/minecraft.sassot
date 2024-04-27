package solipingen.sassot.advancement;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;


public record PlayerSkeweredEntityConditions(Optional<LootContextPredicate> player, Optional<DamagePredicate> damage, Optional<LootContextPredicate> entity) implements AbstractCriterion.Conditions {
    static final Identifier ID = PlayerSkeweredEntityCriterion.ID;
    public static final Codec<PlayerSkeweredEntityConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(PlayerSkeweredEntityConditions::player), 
            Codecs.createStrictOptionalFieldCodec(DamagePredicate.CODEC, "damage").forGetter(PlayerSkeweredEntityConditions::damage), 
            Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "entity").forGetter(PlayerSkeweredEntityConditions::entity))
        .apply(instance, PlayerSkeweredEntityConditions::new));


    public static PlayerSkeweredEntityConditions create() {
        return new PlayerSkeweredEntityConditions(Optional.empty(), Optional.empty(), Optional.empty());
    }

    public static PlayerSkeweredEntityConditions createDamagePredicate(Optional<DamagePredicate> damagePredicate) {
        return new PlayerSkeweredEntityConditions(Optional.empty(), damagePredicate, Optional.empty());
    }

    public static PlayerSkeweredEntityConditions create(DamagePredicate.Builder damagePredicateBuilder) {
        return new PlayerSkeweredEntityConditions(Optional.empty(), Optional.of(damagePredicateBuilder.build()), Optional.empty());
    }

    public static PlayerSkeweredEntityConditions create(Optional<EntityPredicate> hurtEntityPredicate) {
        return new PlayerSkeweredEntityConditions(Optional.empty(), Optional.empty(), EntityPredicate.contextPredicateFromEntityPredicate(hurtEntityPredicate));
    }

    public static PlayerSkeweredEntityConditions create(Optional<DamagePredicate> damagePredicate, Optional<EntityPredicate> hurtEntityPredicate) {
        return new PlayerSkeweredEntityConditions(Optional.empty(), damagePredicate, EntityPredicate.contextPredicateFromEntityPredicate(hurtEntityPredicate));
    }

    public static PlayerSkeweredEntityConditions create(DamagePredicate.Builder damagePredicateBuilder, Optional<EntityPredicate> hurtEntityPredicate) {
        return new PlayerSkeweredEntityConditions(Optional.empty(), Optional.of(damagePredicateBuilder.build()), EntityPredicate.contextPredicateFromEntityPredicate(hurtEntityPredicate));
    }

    public boolean matches(ServerPlayerEntity player, LootContext entityContext, DamageSource source, float dealt, float taken, boolean blocked) {
        if (this.damage.isPresent() && !this.damage.get().test(player, source, dealt, taken, blocked)) {
            return false;
        }
        return !this.entity.isPresent() || this.entity.get().test(entityContext);
    }


}
