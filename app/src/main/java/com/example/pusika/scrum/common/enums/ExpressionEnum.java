package com.example.pusika.scrum.common.enums;

/**
 * Значения выражений для условий {@link com.example.pusika.scrum.model.ConditionOf}
 */
public enum ExpressionEnum {
    /**
     * Строго меньше
     */
    LESS("less"),
    /**
     * Сторого больше
     */
    MORE("more"),
    /**
     * Равно
     */
    EQUALS("equals"),
    /**
     * Не служит для проверки!
     * Значение условия, будет умножено на значение статуса {@link com.example.pusika.scrum.model.Status#value}, которое оно проверяет с сохранением знака.
     * Все "Функции" будут сложены между собой, чтобы определить вероятность применения эффекта.
     */
    FUNCTION("function"),

    /**
     * Достаточно, чтобы было больше некоторого значения
     */
    ENOUGH_MORE("enoughMore"),
    /**
     * Достаточно, чтобы было меньше некоторого значения
     */
    ENOUGH_LESS("enoughLess");
    private String code;

    ExpressionEnum(String code) {
        this.code = code;
    }

    /**
     * Получение enum по соответствующему коду
     *
     * @param code код
     * @return enum
     */
    public static ExpressionEnum of(String code) {
        for (ExpressionEnum expression : values()) {
            if (expression.code.equalsIgnoreCase(code)) {
                return expression;
            }
        }
        throw new IllegalArgumentException("такого выражения нет");
    }

    public String getCode() {
        return code;
    }
}
