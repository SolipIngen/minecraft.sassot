package solipingen.sassot.advancement;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;


public record ThrowSpearConditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
    static final Identifier ID = ThrowSpearCriterion.ID;
    public static final Codec<ThrowSpearConditions> CODEC = RecordCodecBuilder.create((instance) -> 
        instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(ThrowSpearConditions::player),
            ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ThrowSpearConditions::item))
        .apply(instance, ThrowSpearConditions::new));


    public static AdvancementCriterion<ThrowSpearConditions> create(Optional<ItemPredicate> itemPredicate) {
        return ModCriteria.THROW_SPEAR.create(new ThrowSpearConditions(Optional.empty(), itemPredicate));
    }

    public static AdvancementCriterion<ThrowSpearConditions> create(ItemConvertible item) {
        return  ModCriteria.THROW_SPEAR.create(new ThrowSpearConditions(Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build())));
    }

    public boolean matches(ItemStack stack) {
        return this.item.isPresent() && this.item.get().test(stack);
    }



}
