package solipingen.sassot.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import solipingen.sassot.sound.ModSoundEvents;


public class ModShieldItem extends ShieldItem {
    public static final int field_30918 = 5;
    public final float minDamageToBreak;
    public static final String BASE_KEY = "Base";
    private final int enchantability;
    private final boolean isFramed;
    private final Ingredient repairIngredient;
    private final int disabledTicks;
    private final float unyieldingModifier;

    
    public ModShieldItem(ToolMaterial material, float minBreakDamage, boolean isFramedShield, int disabledTicks, float unyieldingModifier, Item.Settings settings) {
        super(settings.maxDamageIfAbsent(MathHelper.ceil((isFramedShield ? 1.125f : 1.85f)*material.getDurability())));
        this.minDamageToBreak = minBreakDamage;
        this.isFramed = isFramedShield;
        this.enchantability = material.getEnchantability();
        this.repairIngredient = this.isFramed ? ToolMaterials.WOOD.getRepairIngredient() : material.getRepairIngredient();
        this.disabledTicks = disabledTicks;
        this.unyieldingModifier = unyieldingModifier;
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (BlockItem.getBlockEntityNbt(stack) != null) {
            return this.getTranslationKey() + "." + ShieldItem.getColor(stack).getName();
        }
        return super.getTranslationKey(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        BannerItem.appendBannerTooltip(stack, tooltip);
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.repairIngredient.test(ingredient) || super.canRepair(stack, ingredient);
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    public float getMinDamageToBreak() {
        return this.minDamageToBreak;
    }

    public int getDisabledTicks() {
        return this.disabledTicks;
    }

    public float getUnyieldingModifier() {
        return this.unyieldingModifier;
    }

    public boolean getIsFramedShield() {
        return this.isFramed;
    }

    public SoundEvent getHitSoundEvent() {
        if (this.isFramed || this.repairIngredient == ToolMaterials.WOOD.getRepairIngredient()) {
            return SoundEvents.ITEM_SHIELD_BLOCK;
        }
        else {
            if (this.repairIngredient == ModToolMaterials.COPPER.getRepairIngredient()) {
                return ModSoundEvents.COPPER_SHIELD_BLOCK;
            }
            else if (this.repairIngredient == ToolMaterials.GOLD.getRepairIngredient()) {
                return ModSoundEvents.GOLDEN_SHIELD_BLOCK;
            }
            else if (this.repairIngredient == ToolMaterials.IRON.getRepairIngredient()) {
                return ModSoundEvents.IRON_SHIELD_BLOCK;
            }
            else if (this.repairIngredient == ToolMaterials.DIAMOND.getRepairIngredient()) {
                return ModSoundEvents.DIAMOND_SHIELD_BLOCK;
            }
            return ModSoundEvents.NETHERITE_SHIELD_BLOCK;
        }
    }

    public static DyeColor getColor(ItemStack stack) {
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(stack);
        return nbtCompound != null ? DyeColor.byId(nbtCompound.getInt(BASE_KEY)) : DyeColor.WHITE;
    }


}
