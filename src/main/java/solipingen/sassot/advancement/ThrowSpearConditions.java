package solipingen.sassot.advancement;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;


public class ThrowSpearConditions extends AbstractCriterionConditions {
    static final Identifier ID = ThrowSpearCriterion.ID;
    private final Optional<ItemPredicate> item;

    public ThrowSpearConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) {
        super(player);
        this.item = item;
    }

    public static ThrowSpearConditions create(Optional<ItemPredicate> itemPredicate) {
        return new ThrowSpearConditions(Optional.empty(), itemPredicate);
    }

    public static ThrowSpearConditions create(ItemConvertible item) {
        return new ThrowSpearConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build()));
    }

    public boolean matches(ItemStack stack) {
        return this.item.isPresent() && this.item.get().test(stack);
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = super.toJson();
        this.item.ifPresent((itemPredicate) -> jsonObject.add("item", itemPredicate.toJson()));
        return jsonObject;
    }

}
