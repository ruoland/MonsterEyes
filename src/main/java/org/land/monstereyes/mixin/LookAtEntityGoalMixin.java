package org.land.monstereyes.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.land.monstereyes.Monstereyes.isInFieldOfView;

@Mixin(net.minecraft.entity.ai.goal.LookAtEntityGoal.class)
public abstract class LookAtEntityGoalMixin extends Goal {

    @Shadow @Final protected MobEntity mob;

    @Shadow @Nullable protected Entity target;

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void injectFieldOfViewCheck(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() != null && cir.getReturnValue()) {
            if(this.target instanceof MobEntity targetEntity) {
                if (!isInFieldOfView(mob, targetEntity)) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
