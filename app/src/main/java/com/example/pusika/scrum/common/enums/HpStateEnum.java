package com.example.pusika.scrum.common.enums;

/**
 * Состояние бойца
 */
public enum HpStateEnum {
    HEALTHY("полность здоров"),
    CUT("ранен"),
    SCRATCH("задет"),
    BAD("плох"),
    DEAD("мертв");
    private String code;

    HpStateEnum(String code) {
        this.code = code;
    }

    /**
     * Получение enum по соответствующему коду
     *
     * @param code код
     * @return enum
     */
    public static HpStateEnum of(String code) {
        for (HpStateEnum hpState : values()) {
            if (hpState.code.equalsIgnoreCase(code)) {
                return hpState;
            }
        }
        throw new IllegalArgumentException("такого состояния нет");
    }

    public String getCode() {
        return code;
    }
}
