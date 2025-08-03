package solipingen.sassot.mixin.entity.mob;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import solipingen.sassot.item.ModItems;
import solipingen.sassot.registry.tag.ModItemTags;
import solipingen.sassot.village.ModVillagerProfessions;

import java.util.List;


@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow @Final private static double ATTACK_RANGE;


    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tryEquip", at = @At("HEAD"), cancellable = true)
    private void injectedTryEquip(ServerWorld world, ItemStack stack, CallbackInfoReturnable<ItemStack> cbireturn) {
        if ((MobEntity)(Object)this instanceof PiglinEntity && stack.getItem() instanceof ShieldItem) {
            cbireturn.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(method = "getPreferredWeapons", at = @At("HEAD"), cancellable = true)
    private void injectedGetPreferredWeapons(CallbackInfoReturnable<TagKey<Item>> cbireturn) {
        if (((MobEntity)(Object)this) instanceof VillagerEntity villagerEntity) {
            VillagerProfession profession = villagerEntity.getVillagerData().profession().value();
            if (profession == ModVillagerProfessions.SWORDSMAN) {
                cbireturn.setReturnValue(ModItemTags.SWORDSMAN_VILLAGER_PREFERRED_WEAPONS);
            }
            else if (profession == ModVillagerProfessions.SPEARMAN) {
                cbireturn.setReturnValue(ModItemTags.SPEARMAN_VILLAGER_PREFERRED_WEAPONS);
            }
        }
    }

    @Inject(method = "getAttackBox", at = @At("HEAD"), cancellable = true)
    private void injectedGetAttackBox(CallbackInfoReturnable<Box> cbireturn) {
        Box box3;
        Entity entity = this.getVehicle();
        if (entity != null) {
            Box box = entity.getBoundingBox();
            Box box2 = this.getBoundingBox();
            box3 = new Box(Math.min(box2.minX, box.minX), box2.minY, Math.min(box2.minZ, box.minZ), Math.max(box2.maxX, box.maxX), box2.maxY, Math.max(box2.maxZ, box.maxZ));
        }
        else {
            box3 = this.getBoundingBox();
        }
        ItemStack mainHandStack = this.getWeaponStack();
        AttributeModifiersComponent component = mainHandStack.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, null);
        double addition = 0.0;
        if (component != null) {
            List<AttributeModifiersComponent.Entry> entries = component.modifiers();
            for (AttributeModifiersComponent.Entry entry : entries) {
                if (entry.attribute().value() == EntityAttributes.ENTITY_INTERACTION_RANGE.value()) {
                    addition = entry.modifier().value();
                }
            }
        }
        cbireturn.setReturnValue(box3.expand(ATTACK_RANGE + addition, addition, ATTACK_RANGE + addition));
    }
    
    


}
