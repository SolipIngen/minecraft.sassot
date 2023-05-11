package solipingen.sassot.advancement;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;


public class ThrowSpearConditions extends AbstractCriterionConditions {
    static final Identifier ID = ThrowSpearCriterion.ID;
    private final ItemPredicate item;

    public ThrowSpearConditions(EntityPredicate.Extended player, ItemPredicate item) {
        super(ID, player);
        this.item = item;
    }

    public static ThrowSpearConditions create(ItemPredicate itemPredicate) {
        return new ThrowSpearConditions(EntityPredicate.Extended.EMPTY, itemPredicate);
    }

    public static ThrowSpearConditions create(ItemConvertible item) {
        return new ThrowSpearConditions(EntityPredicate.Extended.EMPTY, ItemPredicate.Builder.create().items(item).build());
    }

    public boolean matches(ItemStack stack) {
        return this.item.test(stack);
    }

    @Override
    public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
        JsonObject jsonObject = super.toJson(predicateSerializer);
        jsonObject.add("item", this.item.toJson());
        return jsonObject;
    }

}
