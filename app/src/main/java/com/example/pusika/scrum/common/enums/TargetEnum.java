package com.example.pusika.scrum.common.enums;

public enum TargetEnum {
    PLACE("place"),
    HERO("hero"),
    ENEMY("enemy");
    private String code;

    TargetEnum(String code) {
        this.code = code;
    }

    /**
     * Получение enum по соответствующему коду
     *
     * @param code код
     * @return enum
     */
    public static TargetEnum of(String code) {
        for (TargetEnum target : values()) {
            if (target.code.equalsIgnoreCase(code)) {
                return target;
            }
            if (code.isEmpty()) {
                return HERO;
            }
        }
        throw new IllegalArgumentException("такой цели для проверки условия нет");
    }

    public String getCode() {
        return code;
    }


}
