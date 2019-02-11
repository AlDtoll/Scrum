package com.example.pusika.scrum.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Базовый класс бойца
 * У него есть все что нужно бойцу, которому отводится роль неуправляемого противника
 * Может брони не хватает и какого-нибудь поведения в бою, типа серии ударов
 * возможно часть автодействий должно быть тут
 */
public class Fighter implements Serializable {

    //todo подумать над тем, чтобы сделать это списком параметров, так как в некоторых эффектах захочу смотреть эти параметры как статусы
    private int hp;
    private int dmg;
    private int maxHp;
    private ArrayList<Status> statuses = new ArrayList<>();

    private String name;
    private int icon;
    private String description;

    public Fighter() {
    }

    /**
     * @param maxHp максимальное здоровье бойца
     * @param hp    текущее здоровье бойца
     * @param dmg   урон, который он наносит
     */
    public Fighter(int maxHp, int hp, int dmg) {
        this.maxHp = maxHp;
        this.hp = hp;
        this.dmg = dmg;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        //todo возможное превышение здоровья - свойство героя или битвы?
        if (hp > maxHp) {
            this.hp = maxHp;
        } else if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public void decreaseHp(int point) {
        int valueToDecrease;
        if (point < 0) {
            valueToDecrease = -point;
        } else {
            valueToDecrease = point;
        }
        this.hp = this.hp - valueToDecrease;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
