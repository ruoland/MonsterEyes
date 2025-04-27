package org.land.monstereyes.mixin; // ActiveTargetGoalMixin 패키지

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.land.monstereyes.MonsterEntityAttributes;

import org.land.monstereyes.Monstereyes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
// MobEntityMixin에 추가된 isInFieldOfView 메서드를 사용하기 위해 필요


@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin extends TrackTargetGoal {

    @Shadow
    protected LivingEntity targetEntity; // 타겟 필드

    // 생성자 (Mixin 요구사항)
    public ActiveTargetGoalMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    // canStart() 메서드 끝에 주입
    @Inject(method = "canStart", at = @At("TAIL"), cancellable = true) // cancellable = true 추가
    private void injectFieldOfViewCheck(CallbackInfoReturnable<Boolean> cir) {
        // 원래 canStart()의 결과가 true이고, 바닐라 로직이 타겟을 찾았을 때만 시야각 체크를 수행합니다.
        // ActiveTargetGoal은 기본적으로 canSee() 체크를 포함하고 있을 가능성이 높습니다.
        if (cir.getReturnValue() != null && cir.getReturnValue() && this.targetEntity != null) {
            if (!Monstereyes.isInFieldOfView(mob, this.targetEntity)) {
                cir.setReturnValue(false);
            }
        }
    }

}