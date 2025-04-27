package org.land.monstereyes;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class MonsterEntityAttributes {
    // 새로운 시야각(Field of View) Attribute 정의
    public static final RegistryEntry<EntityAttribute> FIELD_OF_VIEW = register("field_of_view", 180.0, 0, 360.0);
    private static RegistryEntry<EntityAttribute> register(final String name, double base, double min, double max) {
        EntityAttribute attribute = new ClampedEntityAttribute("attribute.name." + Monstereyes.MOD_ID + '.' + name, base, min, max).setTracked(true);
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(Monstereyes.MOD_ID, name), attribute);
    }

    public static void registerAttributes() {

    }
}