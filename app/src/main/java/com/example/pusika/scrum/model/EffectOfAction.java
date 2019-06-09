package com.example.pusika.scrum.model;

import com.example.pusika.scrum.common.enums.EffectEnum;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Эффект действия
 */
public class EffectOfAction implements Serializable {

    /**
     * Возможный эффект
     */
    private EffectEnum effect;
    /**
     * Если эффект заключается в смене статуса, то тут должно быть имя изменяемого статуса
     */
    //todo сделать проверку, чтобы при changeStatus был статус
    private Status status;
    /**
     * Значение, на которое нужно изменить выбранный параметр.
     * Для статуса:
     * Если такого статуса нет, то его значение считается за 0
     */
    private String value;
    /**
     * Условия {@link ConditionOf} примения эффекта
     */
    private ArrayList<ConditionOf> conditionsOfEffect = new ArrayList<>();
    /**
     * Сообщение, которое будет указано при успехе
     */
    private String success;
    /**
     * Сообщение, которое будет показано при провале
     */
    private String fail;

    //todo почистить конструкторы

    public EffectOfAction() {
    }

    public EffectEnum getEffect() {
        return effect;
    }

    public void setEffect(EffectEnum effect) {
        this.effect = effect;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<ConditionOf> getConditionsOfEffect() {
        return conditionsOfEffect;
    }

    public void setConditionsOfEffect(ArrayList<ConditionOf> conditionsOfEffect) {
        this.conditionsOfEffect = conditionsOfEffect;
    }

    public String getSuccess() {
        return success != null ? success : "";
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFail() {
        return fail != null ? fail : "";
    }

    public void setFail(String fail) {
        this.fail = fail;
    }
}


