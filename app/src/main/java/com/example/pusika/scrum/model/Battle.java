package com.example.pusika.scrum.model;

public class Battle {

    private Fighter hero;
    private Fighter enemy;
    private int hits = 0;
    private int cuts = 0;
    private int blocks = 0;
    private int misses = 0;

    private String code;
    private String title;
    private Place place;

    //todo не уверен в необходимости
    public Battle(Fighter hero, Fighter enemy) {
        this.hero = hero;
        this.enemy = enemy;
    }

    public void getHit(int point) {
        hero.decreaseHp(point);
        cuts++;
    }

    public void strike(int point) {
        enemy.decreaseHp(point);
        hits++;
    }

    public int getHeroHp() {
        return hero.getHp();
    }

    public void setHeroHp(int value) {
        hero.setHp(value);
    }

    public int getEnemyHp() {
        return enemy.getHp();
    }

    public void setEnemyHp(int value) {
        enemy.setHp(value);
    }

    public int getHits() {
        return hits;
    }

    public int getCuts() {
        return cuts;
    }

    public void startRound() {
        hits = 0;
        cuts = 0;
        blocks = 0;
        misses = 0;
    }

    public void block() {
        blocks++;
    }

    public int getBlocks() {
        return blocks;
    }

    public void miss() {
        misses++;
    }

    public int getMisses() {
        return misses;
    }
}
