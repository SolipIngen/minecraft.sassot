package solipingen.sassot.mixin.enchantment;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.enchantment.Enchantment;
import solipingen.sassot.registry.tag.ModItemTags;

import java.util.Optional;


@Mixin(DamageEnchantment.class)
public abstract class DamageEnchantmentMixin extends Enchantment {
    @Shadow @Final private Optional<TagKey<EntityType<?>>> applicableEntities;


    protected DamageEnchantmentMixin(Enchantment.Properties properties) {
        super(properties);
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static Enchantment.Properties modifiedInit(Enchantment.Properties properties) {
        if (properties.supportedItems() == ItemTags.TRIDENT_ENCHANTABLE || properties.supportedItems() == ModItemTags.BLAZEARM_ENCHANTABLE) {
            return properties;
        }
        return Enchantment.properties(ItemTags.SHARP_WEAPON_ENCHANTABLE, properties.weight(), properties.maxLevel(), properties.minCost(), properties.maxCost(), properties.anvilCost(), properties.slots());
    }

    @Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
    private void injectedGetAttackDamage(int level, @Nullable EntityType<?> entityType, CallbackInfoReturnable<Float> cbireturn) {
        if (this.applicableEntities.isEmpty()) {
            cbireturn.setReturnValue(level*1.0f);
        }
    }

    @Inject(method = "canAccept", at = @At("HEAD"), cancellable = true)
    private void injectedCanAccept(Enchantment other, CallbackInfoReturnable<Boolean> cbireturn) {
        if (this.getApplicableItems() == ItemTags.SHARP_WEAPON_ENCHANTABLE) {
            cbireturn.setReturnValue(other.getApplicableItems() != ItemTags.SHARP_WEAPON_ENCHANTABLE);
        }
        else if (this.getApplicableItems() == ItemTags.TRIDENT_ENCHANTABLE) {
            cbireturn.setReturnValue(other.getApplicableItems() != ModItemTags.BLAZEARM_ENCHANTABLE);
        }
        else if (this.getApplicableItems() == ModItemTags.BLAZEARM_ENCHANTABLE) {
            cbireturn.setReturnValue(other.getApplicableItems() != ItemTags.TRIDENT_ENCHANTABLE);
        }
    }

    
}
