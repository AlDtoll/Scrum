package com.example.pusika.scrum.model;

import com.example.pusika.scrum.common.enums.ExpressionEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Условие эффекта применяемого действия {@link EffectOfAction}, либо доступности самого действия {@link ScrumAction}
 */
public class ConditionOf implements Serializable {

    /**
     * Имя проверяемого статуса. Если такой статус находится, то смотрится его значение в соответствии с выражением
     */
    private String statusName;
    private ExpressionEnum expression;
    private int value;

    public ConditionOf() {
    }

    //todo переделать
    public ConditionOf(ExpressionEnum expression) {
        this(ExpressionEnum.ANY, "", 0);
    }

    public ConditionOf(ExpressionEnum expression, String statusName, int value) {
        this.statusName = statusName;
        this.expression = expression;
        this.value = value;
    }

    public static boolean isCondition(ArrayList<ConditionOf> conditionsOfAction, ArrayList<Status> heroStatuses) {
        if (conditionsOfAction.size() == 0) {
            return true;
        }
        int point = 0;
        for (ConditionOf conditionOf : conditionsOfAction) {
            for (Status status : heroStatuses) {
                if (status.getName().equalsIgnoreCase(conditionOf.getStatusName())) {
                    if (conditionOf.getExpression() == (ExpressionEnum.MORE)) {
                        if (status.getValue() <= conditionOf.getValue()) {
                            return false;
                        }
                    }
                    if (conditionOf.getExpression() == (ExpressionEnum.LESS)) {
                        if (status.getValue() >= conditionOf.getValue()) {
                            return false;
                        }
                    }
                    if (conditionOf.getExpression() == ExpressionEnum.FUNCTION) {
                        point += status.getValue() * conditionOf.getValue();
                    } else {
                        return true;
                    }
                }
            }
        }
        Random random = new Random(new Date().getTime());
        int percent = random.nextInt(100) + 1;
        return point > percent;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public ExpressionEnum getExpression() {
        return expression;
    }

    public void setExpression(ExpressionEnum expression) {
        this.expression = expression;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
