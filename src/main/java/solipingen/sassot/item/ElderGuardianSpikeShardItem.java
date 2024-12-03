package solipingen.sassot.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;


public class ElderGuardianSpikeShardItem extends Item {
    public static final int ATTACK_DAMAGE = 1;
    public static final float ATTACK_SPEED = -1.0f;


    public ElderGuardianSpikeShardItem(int attackDamage, float attackSpeed, Settings settings) {
        super(settings);
    }

    public static AttributeModifiersComponent createAttributeModifiers() {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, ATTACK_DAMAGE, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED,
                        new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, ATTACK_SPEED, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 300 + attacker.getRandom().nextInt(300), 0));
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        if (stack.isEmpty()) {
            attacker.setStackInHand(Hand.MAIN_HAND, new ItemStack(ModItems.ELDER_GUARDIAN_SPIKE_BONE_SHARD, attacker.getRandom().nextBetween(1, 3)));
        }
        return true;
    }


    
}
