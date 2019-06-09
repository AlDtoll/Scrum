package com.example.pusika.scrum.common.enums;

/**
 * Список эффектов {@link com.example.pusika.scrum.model.EffectOfAction}, которые могут иметь действия {@link com.example.pusika.scrum.model.ScrumAction}
 */
public enum EffectEnum {
    /**
     * Изменить значение Hp героя
     */
    CHANGE_HERO_HP("changeHeroHp"),
    /**
     * Изменить (добавить) статус героя
     */
    CHANGE_HERO_STATUS("changeHeroStatus"),
    /**
     * Изменить (добавить) статус врага
     */
    CHANGE_ENEMY_STATUS("changeEnemyStatus"),
    /**
     * Изменить значение Hp противника
     */
    CHANGE_ENEMY_HP("changeEnemyHp"),
    /**
     * Изменить значение времени, которое есть на начало раунда
     */
    CHANGE_TIME("changeTime"),
    /**
     * Изменить длительность раунда
     */
    CHANGE_ROUND_TIME("changeRoundTime"),

    SET_HERO_STATUS("setHeroStatus"),
    SET_ENEMY_STATUS("setEnemyStatus");
    private String code;

    EffectEnum(String code) {
        this.code = code;
    }

    /**
     * Получение enum по соответствующему коду
     *
     * @param code код
     * @return enum
     */
    public static EffectEnum of(String code) {
        for (EffectEnum effect : values()) {
            if (effect.code.equalsIgnoreCase(code)) {
                return effect;
            }
        }
        throw new IllegalArgumentException("такого эффекта нет");
    }

    public String getCode() {
        return code;
    }
}
