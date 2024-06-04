package solipingen.sassot.mixin.item;

import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import solipingen.sassot.enchantment.ModEnchantments;
import solipingen.sassot.registry.tag.ModItemTags;

import java.util.function.Consumer;


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements FabricItemStack {

    @Shadow
    public abstract boolean isOf(Item item);

    @Shadow
    @Nullable
    public abstract <T> T set(DataComponentType<? super T> type, @Nullable T value);

    @Shadow
    public abstract boolean isIn(TagKey<Item> tag);


    @Inject(method = "<init>(Lnet/minecraft/item/ItemConvertible;ILnet/minecraft/component/ComponentMapImpl;)V", at = @At("TAIL"))
    private void injecetedInit(ItemConvertible item, int count, ComponentMapImpl components, CallbackInfo cbi) {
        if (item == Items.BLAZE_ROD || item == Items.BLAZE_POWDER || item == Items.END_ROD) {
            this.set(DataComponentTypes.FIRE_RESISTANT, Unit.INSTANCE);
        }
    }

    @Inject(method = "appendAttributeModifierTooltip", at = @At("HEAD"), cancellable = true)
    private void injectedAttackSpeedAttributeValue(Consumer<Text> textConsumer, @Nullable PlayerEntity player, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier, CallbackInfo cbi) {
        double d = modifier.value();
        boolean bl = false;
        if (player != null) {
            if (modifier.uuid() == Item.ATTACK_DAMAGE_MODIFIER_ID) {
                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                d += EnchantmentHelper.getAttackDamage((ItemStack)(Object)this, null);
                bl = true;
            }
            else if (modifier.uuid() == Item.ATTACK_SPEED_MODIFIER_ID) {
                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
                if (this.isIn(ModItemTags.SWEEPING_WEAPONS)) {
                    d += 0.1*EnchantmentHelper.getLevel(Enchantments.SWEEPING_EDGE, (ItemStack)(Object)this);
                }
                else if (this.isIn(ModItemTags.THRUSTING_WEAPONS)) {
                    d += 0.1*EnchantmentHelper.getLevel(ModEnchantments.THRUSTING, (ItemStack)(Object)this);
                }
                else if (this.isIn(ModItemTags.HACKING_WEAPONS)) {
                    d += 0.1*EnchantmentHelper.getLevel(ModEnchantments.HACKING, (ItemStack)(Object)this);
                }
                bl = true;
            }
        }
        double e = modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE || modifier.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL ? d * 100.0 : (attribute.matches(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) ? d * 10.0 : d);
        if (bl) {
            textConsumer.accept(ScreenTexts.space().append(Text.translatable("attribute.modifier.equals." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(e), Text.translatable(attribute.value().getTranslationKey()))).formatted(Formatting.DARK_GREEN));
        }
        else if (d > 0.0) {
            textConsumer.accept(Text.translatable("attribute.modifier.plus." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(e), Text.translatable(attribute.value().getTranslationKey())).formatted(Formatting.BLUE));
        }
        else if (d < 0.0) {
            textConsumer.accept(Text.translatable("attribute.modifier.take." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(-e), Text.translatable(attribute.value().getTranslationKey())).formatted(Formatting.RED));
        }
        cbi.cancel();
    }

//    @Inject(method = "getMaxDamage", at = @At("HEAD"), cancellable = true)
//    private void injectedGetMaxDamage(CallbackInfoReturnable<Integer> cbireturn) {
//        if (this.isOf(Items.TRIDENT) || this.isOf(Items.MACE)) {
//            cbireturn.setReturnValue(1095);
//        }
//    }



}
