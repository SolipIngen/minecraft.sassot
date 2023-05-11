package solipingen.sassot.item;

import java.util.function.Supplier;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

@SuppressWarnings("deprecation")
public enum ModToolMaterials implements ToolMaterial {
    BONE(ModMiningLevels.WOOD, 108, 4.0f, 1.0f, 9, () -> Ingredient.ofItems(Items.BONE)), 
    COPPER(ModMiningLevels.COPPER, 250, 6.0f, 1.0f, 14, () -> Ingredient.ofItems(Items.COPPER_INGOT)), 
    EMERALD(ModMiningLevels.IRON, 420, 4.0f, 1.0f, 15, () -> Ingredient.ofItems(Items.EMERALD));

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairIngredient;


    private ModToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
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
    public int getMiningLevel() {
        return this.miningLevel;
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
