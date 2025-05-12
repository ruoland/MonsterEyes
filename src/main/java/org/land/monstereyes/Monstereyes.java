package org.land.monstereyes;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;


import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.argument;

public class Monstereyes implements ModInitializer {

    public static final String MOD_ID = "monstereyes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID); // Logger 선언
    public static MonsterEyesConfig CONFIG;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing MonsterEyes!"); // 로그 추가

        // AutoConfig 등록 및 로드
        AutoConfig.register(MonsterEyesConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(MonsterEyesConfig.class).getConfig();

        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) ->
        {
            dispatcher.register(CommandManager.literal("monstereyes")
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2)) // 오피 레벨 2 이상 필요
                    .then(CommandManager.literal("reload")
                            .executes(context -> {
                                try {
                                    // 설정 파일 리로드
                                    boolean success = AutoConfig.getConfigHolder(MonsterEyesConfig.class).load();

                                    if (success) {
                                        context.getSource().sendFeedback(() -> Text.literal("§a[MonsterEyes] Config reloaded successfully!"), false);

                                        CONFIG = AutoConfig.getConfigHolder(MonsterEyesConfig.class).getConfig();;
                                        return 1; // 성공 반환
                                    } else {
                                        context.getSource().sendFeedback(() -> Text.literal("§c[MonsterEyes] Failed to reload config: Load operation failed."), false);
                                        return 0; // 실패 반환
                                    }

                                } catch (Exception e) {
                                    // 리로드 중 예외 발생 시
                                    context.getSource().sendFeedback(() -> Text.literal("§c[MonsterEyes] Failed to reload config: " + e.getMessage()), false);
                                    e.printStackTrace(); // 오류 추적을 위해 스택 트레이스 출력
                                    return 0; // 실패 반환
                                }
                            })
                    )
            );
        });

    }



    public static boolean isInFieldOfView(LivingEntity mob, LivingEntity target) {
        double fieldOfViewDegrees;
        String mobId = Registries.ENTITY_TYPE.getId(mob.getType()).toString(); // 몬스터 타입의 Identifier 문자열 가져오기


        if (Monstereyes.CONFIG.perMonsterFieldOfView.containsKey(mobId)) {
            fieldOfViewDegrees = Monstereyes.CONFIG.perMonsterFieldOfView.get(mobId);
            fieldOfViewDegrees = MathHelper.clamp(fieldOfViewDegrees, 0.0, 360.0);
        }
        else {
            fieldOfViewDegrees = Monstereyes.CONFIG.defaultFieldOfView;
            fieldOfViewDegrees = MathHelper.clamp(fieldOfViewDegrees, 0.0, 360.0);
        }


        if (fieldOfViewDegrees >= 360.0) {
            return mob.canSee(target); // 시야 차단 블록만 체크
        }

        if (!mob.canSee(target)) {
            return false; // 시야를 가리는 블록이 있으면 보지 못함
        }

        Vec3d mobLookVec = mob.getRotationVector(mob.getPitch(), mob.getHeadYaw());

        // 타겟의 눈 위치에서 몬스터의 눈 위치로 향하는 벡터를 구하고 정규화합니다.
        Vec3d targetDirection = target.getEyePos().subtract(mob.getEyePos()).normalize();

        // 두 벡터의 내적을 구합니다. 내적 = cos(theta)
        double dotProduct = mobLookVec.dotProduct(targetDirection);

        // cos(theta) 값을 각도(라디안)으로 변환합니다. acos의 인수는 -1.0 ~ 1.0 사이여야 합니다.
        double angleRadians = Math.acos(MathHelper.clamp(dotProduct, -1.0, 1.0));

        // 라디안을 도로 변환합니다.
        double angleDegrees = Math.toDegrees(angleRadians);

        // 설정된 시야각의 절반 값과 비교합니다.
        double halfFOV = fieldOfViewDegrees / 2.0;

        boolean isInFOV = angleDegrees <= halfFOV;

        return isInFOV;
    }
}