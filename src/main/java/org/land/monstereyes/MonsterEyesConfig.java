package org.land.monstereyes;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.Map;
import java.util.HashMap;

@Config(name = Monstereyes.MOD_ID)
public class MonsterEyesConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip // GUI에서 툴팁 표시
    @ConfigEntry.BoundedDiscrete(min = 0L, max = 360L)
    public double defaultFieldOfView = 120.0;

    @ConfigEntry.Gui.Tooltip
    public Map<String, Double> perMonsterFieldOfView = new HashMap<>();


    @ConfigEntry.Gui.Tooltip
    @Comment
    @ConfigEntry.BoundedDiscrete(min = 1L, max = 100L)
    public double defaultBackstabDamageMultiplier = 1.3;

    public MonsterEyesConfig() {

        perMonsterFieldOfView.put("minecraft:zombie", 120.0);
        perMonsterFieldOfView.put("minecraft:skeleton", 110.0);
        perMonsterFieldOfView.put("minecraft:creeper", 120.0);
    }

}