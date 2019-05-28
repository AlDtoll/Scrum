package com.example.pusika.scrum.model;

import android.os.Bundle;

import java.util.ArrayList;

import static com.example.pusika.scrum.view.StartActivity.AUTO_ACTIONS;
import static com.example.pusika.scrum.view.StartActivity.ENEMY;
import static com.example.pusika.scrum.view.StartActivity.HERO;
import static com.example.pusika.scrum.view.StartActivity.HERO_ACTIONS;
import static com.example.pusika.scrum.view.StartActivity.PLACE;

public class Scene {

    private Hero hero;
    private Fighter enemy;
    private int hits = 0;
    private int cuts = 0;
    private int blocks = 0;
    private int misses = 0;

    private String code;
    private String title;
    private Place place;

    private ArrayList<ScrumAction> scrumHeroActions = new ArrayList<>();
    private ArrayList<ScrumAction> scrumAutoActions = new ArrayList<>();

    //todo не уверен в необходимости
    public Scene(Hero hero, Fighter enemy) {
        this.hero = hero;
        this.enemy = enemy;
    }

    public Scene() {

    }

    public Scene(Hero hero, Fighter enemy, Place place, ArrayList<ScrumAction> scrumHeroActions, ArrayList<ScrumAction> scrumAutoActions) {
        this.hero = hero;
        this.enemy = enemy;
        this.place = place;
        this.scrumHeroActions = scrumHeroActions;
        this.scrumAutoActions = scrumAutoActions;
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

    public void readScenario(Bundle actors) {
        this.place = (Place) actors.get(PLACE);
        this.hero = (Hero) actors.get(HERO);
        this.enemy = (Fighter) actors.get(ENEMY);
        this.scrumHeroActions = (ArrayList<ScrumAction>) actors.get(HERO_ACTIONS);
        this.scrumAutoActions = (ArrayList<ScrumAction>) actors.get(AUTO_ACTIONS);
    }

    public Hero getHero() {
        return hero;
    }

    public Fighter getEnemy() {
        return enemy;
    }

    public Place getPlace() {
        return place;
    }

    public ArrayList<ScrumAction> getScrumHeroActions() {
        return scrumHeroActions;
    }

    public ArrayList<ScrumAction> getScrumAutoActions() {
        return scrumAutoActions;
    }
}
