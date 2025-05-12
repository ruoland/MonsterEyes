package org.land.monstereyes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.mob.MobEntity; // MobEntity 임포트 추가
import org.land.monstereyes.Monstereyes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityBackstabMixin {

    @ModifyVariable(
            method = "damage(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            argsOnly = true,
            index = 3
    )
    private float monstereyes$applyBackstab(float amount, ServerWorld world, DamageSource source) {
        LivingEntity target = (LivingEntity)(Object)this; //공격 당한 이

        Entity attacker = source.getAttacker(); //공격자
        double multiplierDouble = Monstereyes.CONFIG.defaultBackstabDamageMultiplier;


        if(multiplierDouble <= 1)
            return amount;

        if (attacker instanceof LivingEntity attackerLiving && attackerLiving != target) {

            boolean isEligibleForBackstab = false;

            //공격 당한 이의 시야에 공격자가 있는가?
            if(!Monstereyes.isInFieldOfView(target, attackerLiving)) {
                //없을 경우 추가 데미지
                isEligibleForBackstab = true;
            }


            if (isEligibleForBackstab) {
                return (float) (amount * multiplierDouble);
            }
        }

        return amount;
    }
}