package org.land.monstereyes.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;

import org.land.monstereyes.Monstereyes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin extends TrackTargetGoal {

    @Shadow
    protected LivingEntity targetEntity;

    public ActiveTargetGoalMixin(MobEntity mob, boolean checkVisibility) {
        super(mob, checkVisibility);
    }

    @Inject(method = "canStart", at = @At("TAIL"), cancellable = true) // cancellable = true 추가
    private void injectFieldOfViewCheck(CallbackInfoReturnable<Boolean> cir) {

        if (cir.getReturnValue() != null && cir.getReturnValue() && this.targetEntity != null) {
            if (!Monstereyes.isInFieldOfView(mob, this.targetEntity)) {
                cir.setReturnValue(false);
            }

        }
    }

}