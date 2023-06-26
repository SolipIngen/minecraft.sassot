package solipingen.sassot.advancement;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.DamagePredicate;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;


public class PlayerSkeweredEntityConditions extends AbstractCriterionConditions {
    static final Identifier ID = PlayerSkeweredEntityCriterion.ID;
    private final DamagePredicate damage;
    private final LootContextPredicate entity;

    
    public PlayerSkeweredEntityConditions(LootContextPredicate player, DamagePredicate damage, LootContextPredicate entity) {
        super(ID, player);
        this.damage = damage;
        this.entity = entity;
    }

    public static PlayerSkeweredEntityConditions create() {
        return new PlayerSkeweredEntityConditions(LootContextPredicate.EMPTY, DamagePredicate.ANY, LootContextPredicate.EMPTY);
    }

    public static PlayerSkeweredEntityConditions create(DamagePredicate damagePredicate) {
        return new PlayerSkeweredEntityConditions(LootContextPredicate.EMPTY, damagePredicate, LootContextPredicate.EMPTY);
    }

    public static PlayerSkeweredEntityConditions create(DamagePredicate.Builder damagePredicateBuilder) {
        return new PlayerSkeweredEntityConditions(LootContextPredicate.EMPTY, damagePredicateBuilder.build(), LootContextPredicate.EMPTY);
    }

    public static PlayerSkeweredEntityConditions create(EntityPredicate hurtEntityPredicate) {
        return new PlayerSkeweredEntityConditions(LootContextPredicate.EMPTY, DamagePredicate.ANY, EntityPredicate.asLootContextPredicate(hurtEntityPredicate));
    }

    public static PlayerSkeweredEntityConditions create(DamagePredicate damagePredicate, EntityPredicate hurtEntityPredicate) {
        return new PlayerSkeweredEntityConditions(LootContextPredicate.EMPTY, damagePredicate, EntityPredicate.asLootContextPredicate(hurtEntityPredicate));
    }

    public static PlayerSkeweredEntityConditions create(DamagePredicate.Builder damagePredicateBuilder, EntityPredicate hurtEntityPredicate) {
        return new PlayerSkeweredEntityConditions(LootContextPredicate.EMPTY, damagePredicateBuilder.build(), EntityPredicate.asLootContextPredicate(hurtEntityPredicate));
    }

    public boolean matches(ServerPlayerEntity player, LootContext entityContext, DamageSource source, float dealt, float taken, boolean blocked) {
        if (!this.damage.test(player, source, dealt, taken, blocked)) {
            return false;
        }
        return this.entity.test(entityContext);
    }

    @Override
    public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
        JsonObject jsonObject = super.toJson(predicateSerializer);
        jsonObject.add("damage", this.damage.toJson());
        jsonObject.add("entity", this.entity.toJson(predicateSerializer));
        return jsonObject;
    }
}
