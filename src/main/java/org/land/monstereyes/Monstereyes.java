package org.land.monstereyes;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Monstereyes implements ModInitializer {

    public static final String MOD_ID = "monstereyes";
    @Override
    public void onInitialize() {
    }

    public static boolean isInFieldOfView(LivingEntity mob, LivingEntity target) {
        RegistryEntry<EntityAttribute> fieldOfViewAttribute = MonsterEntityAttributes.FIELD_OF_VIEW;

        if (!mob.getAttributes().hasAttribute(fieldOfViewAttribute)) {
            return true;
        }

        double fovDegrees =mob.getAttributeValue(fieldOfViewAttribute);

        if (fovDegrees >= 360.0) {
            return true;
        }

        Vec3d eyePos = mob.getEyePos();
        Vec3d targetEyePos = target.getEyePos();


        Vec3d lookVector = mob.getRotationVector();

        Vec3d targetDirection = targetEyePos.subtract(eyePos);

        double dotProduct = lookVector.dotProduct(targetDirection.normalize());

        double clampedDotProduct = MathHelper.clamp(dotProduct, -1.0, 1.0);

        double angleRadians = Math.acos(clampedDotProduct);

        double halfFovRadians = Math.toRadians(fovDegrees / 2.0);

        return angleRadians <= halfFovRadians;
    }
}
