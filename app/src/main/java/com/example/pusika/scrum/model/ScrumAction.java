package com.example.pusika.scrum.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Действие, которое может быть доступно герою (а также автоматически совершается) между раундами
 */
public class ScrumAction implements Serializable {

    private int iconOfAction;
    private String nameOfAction;
    private String descriptionOfAction;
    private ArrayList<EffectOfAction> effectsOfAction = new ArrayList<>();
    private ArrayList<ConditionOf> conditionsOfAction = new ArrayList<>();

    public ScrumAction() {
    }

    public int getIconOfAction() {
        return iconOfAction;
    }

    public void setIconOfAction(int iconOfAction) {
        this.iconOfAction = iconOfAction;
    }

    public String getNameOfAction() {
        return nameOfAction;
    }

    public void setNameOfAction(String nameOfAction) {
        this.nameOfAction = nameOfAction;
    }

    public String getDescriptionOfAction() {
        return descriptionOfAction;
    }

    public void setDescriptionOfAction(String descriptionOfAction) {
        this.descriptionOfAction = descriptionOfAction;
    }

    public ArrayList<EffectOfAction> getEffectsOfAction() {
        return effectsOfAction;
    }

    public void setEffectsOfAction(ArrayList<EffectOfAction> effectsOfAction) {
        this.effectsOfAction = effectsOfAction;
    }

    public ArrayList<ConditionOf> getConditionsOfAction() {
        return conditionsOfAction;
    }

    public void setConditionsOfAction(ArrayList<ConditionOf> conditionsOfAction) {
        this.conditionsOfAction = conditionsOfAction;
    }
}
