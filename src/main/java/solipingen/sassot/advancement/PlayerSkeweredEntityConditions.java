package solipingen.sassot.advancement;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class PlayerSkeweredEntityConditions extends AbstractCriterionConditions {
    static final Identifier ID = PlayerSkeweredEntityCriterion.ID;
    private final Optional<DamagePredicate> damage;
    private final Optional<LootContextPredicate> entity;

    
    public PlayerSkeweredEntityConditions(Optional<LootContextPredicate> player, Optional<DamagePredicate> damage, Optional<LootContextPredicate> entity) {
        super(player);
        this.damage = damage;
        this.entity = entity;
    }

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

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = super.toJson();
        this.damage.ifPresent((damagePredicate) -> jsonObject.add("damage", damagePredicate.toJson()));
        this.entity.ifPresent((lootContextPredicate) -> jsonObject.add("entity", lootContextPredicate.toJson()));
        return jsonObject;
    }
}
