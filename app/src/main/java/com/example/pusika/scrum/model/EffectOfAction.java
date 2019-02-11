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
    private int value;
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

    public EffectOfAction(EffectEnum effect, int value) {
        this(effect, null, value, new ArrayList<>());
    }

    public EffectOfAction(EffectEnum effect, Status status, int value, ArrayList<ConditionOf> conditionsOfEffect) {
        this.effect = effect;
        this.value = value;
        this.status = status;
        this.conditionsOfEffect = conditionsOfEffect;
    }

    public EffectOfAction(EffectEnum effect, Status status, int value, ArrayList<ConditionOf> conditionsOfEffect, String success, String fail) {
        this.effect = effect;
        this.value = value;
        this.status = status;
        this.conditionsOfEffect = conditionsOfEffect;
        this.success = success;
        this.fail = fail;
    }

    public EffectOfAction(EffectEnum effect, Status status, String description) {
        this.effect = effect;
        this.status = status;
        this.success = description;
    }

    public EffectEnum getEffect() {
        return effect;
    }

    public void setEffect(EffectEnum effect) {
        this.effect = effect;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFail() {
        return fail;
    }

    public void setFail(String fail) {
        this.fail = fail;
    }
}


