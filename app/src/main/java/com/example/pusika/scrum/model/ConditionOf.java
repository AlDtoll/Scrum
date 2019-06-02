package com.example.pusika.scrum.model;

import com.example.pusika.scrum.common.enums.ExpressionEnum;
import com.example.pusika.scrum.common.enums.TargetEnum;

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
    private TargetEnum targetForCheck;
    private String statusName;
    private ExpressionEnum expression;
    private String value;

    public static boolean isCondition(ArrayList<ConditionOf> conditionOfs, Scene scene) {
        if (conditionOfs.size() == 0) {
            return true;
        }
        int point = 0;
        for (ConditionOf conditionOf : conditionOfs) {
            ArrayList<Status> statuses;
            if (conditionOf.getTargetForCheck() == TargetEnum.ENEMY) {
                statuses = scene.getEnemy().getStatuses();
            } else if (conditionOf.getTargetForCheck() == TargetEnum.PLACE) {
                statuses = scene.getPlace().getStatuses();
            } else {
                statuses = scene.getHero().getStatuses();
            }
            for (Status status : statuses) {
                if (status.getName().equalsIgnoreCase(conditionOf.getStatusName())) {
                    int statusValue = status.getValue();
                    int value;
                    String conditionOfValue = conditionOf.getValue();
                    if (isNumeric(conditionOfValue)) {
                        value = Integer.parseInt(conditionOfValue);
                    } else {
                        value = findValue(conditionOfValue, scene);
                    }
                    if (conditionOf.getExpression() == (ExpressionEnum.ENOUGH_MORE)) {
                        if (statusValue >= value) {
                            return true;
                        }
                    }
                    if (conditionOf.getExpression() == (ExpressionEnum.ENOUGH_LESS)) {
                        if (statusValue <= value) {
                            return true;
                        }
                    }
                    if (conditionOf.getExpression() == (ExpressionEnum.MORE)) {
                        if (statusValue <= value) {
                            return false;
                        }
                    }
                    if (conditionOf.getExpression() == (ExpressionEnum.LESS)) {
                        if (statusValue >= value) {
                            return false;
                        }
                    }
                    if (conditionOf.getExpression() == ExpressionEnum.FUNCTION) {
                        point += statusValue * value;
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

    private static boolean isNumeric(String string) {
        return string.matches("\\d+");
    }

    private static int findValue(String effectOfActionValue, Scene scene) {
        //todo заменить на конкретную проверку
        ArrayList<Status> statuses = scene.getHero().getStatuses();
        for (Status status : statuses) {
            if (status.getName().equals(effectOfActionValue)) {
                return status.getValue();
            }
        }
        return 0;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TargetEnum getTargetForCheck() {
        return targetForCheck;
    }

    public void setTargetForCheck(TargetEnum targetForCheck) {
        this.targetForCheck = targetForCheck;
    }
}
