package solipingen.sassot.item;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Lazy;
import solipingen.sassot.registry.tag.ModBlockTags;

@SuppressWarnings("deprecation")
public enum ModToolMaterials implements ToolMaterial {
    COPPER(ModBlockTags.INCORRECT_FOR_COPPER_TOOL, 250, 6.0f, 1.0f, 14, () -> Ingredient.ofItems(Items.COPPER_INGOT)),
    EMERALD(ModBlockTags.INCORRECT_FOR_EMERALD_TOOL, 420, 4.0f, 1.0f, 15, () -> Ingredient.ofItems(Items.EMERALD));

    private final TagKey<Block> inverseTag;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairIngredient;


    private ModToolMaterials(TagKey<Block> inverseTag, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.inverseTag = inverseTag;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = new Lazy<Ingredient>(repairIngredient);
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return this.inverseTag;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
