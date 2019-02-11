package com.example.pusika.scrum.model;

/**
 * А это герой.
 * Поскольку он главный badass истории, то время, которое дается после выполнения различных действий,
 * зависит от него.
 * Еще от него зависит защищается он или атакует
 */
public class Hero extends Fighter {

    private int timeAfterAttack;
    private int timeAfterDefence;
    private int timeAfterGetHit;
    private int threshold;

    public Hero() {
    }

    /**
     * @param maxHp            максимальное здоровье бойца
     * @param hp               текущее здоровье бойца
     * @param dmg              урон, который он наносит
     * @param timeAfterAttack  время, которое он получит после атаки для нового действия в милисекундах
     * @param timeAfterDefence время, которое он получит после защиты для нового действия в милисекундах
     * @param timeAfterGetHit  время, которое он получит после получения удара для нового действия в милисекундах
     * @param threshold        запас времени, указанный в милисекундах, который должен быть у бойца, чтобы перейти от защиты к нападению
     */
    public Hero(int maxHp, int hp, int dmg, int timeAfterAttack, int timeAfterDefence, int timeAfterGetHit, int threshold) {
        super(maxHp, hp, dmg);
        this.timeAfterAttack = timeAfterAttack;
        this.timeAfterDefence = timeAfterDefence;
        this.timeAfterGetHit = timeAfterGetHit;
        this.threshold = threshold;
    }

    public int getTimeAfterAttack() {
        return timeAfterAttack;
    }

    public void setTimeAfterAttack(int timeAfterAttack) {
        this.timeAfterAttack = timeAfterAttack;
    }

    public int getTimeAfterDefence() {
        return timeAfterDefence;
    }

    public void setTimeAfterDefence(int timeAfterDefence) {
        this.timeAfterDefence = timeAfterDefence;
    }

    public int getTimeAfterGetHit() {
        return timeAfterGetHit;
    }

    public void setTimeAfterGetHit(int timeAfterGetHit) {
        this.timeAfterGetHit = timeAfterGetHit;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
