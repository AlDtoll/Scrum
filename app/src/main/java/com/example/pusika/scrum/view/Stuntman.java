package com.example.pusika.scrum.view;

import com.example.pusika.scrum.model.Status;

import java.util.ArrayList;

/**
 * Нужен, чтобы выполнять различные эффекты
 */
public interface Stuntman {

    //todo надо разбить на эффекты работающие с отдельными свойствами модели: временем, шкалами и т.д.
    //todo подумать, о том чтобы разбить на два интерфейса, один из которых работает с моделью, а второй с презентером (когда будет)

    void changeHeroHp(int value);

    void changeEnemyHp(int value);

    void changeTime(int value);

    void changeRoundTime(int value);

    void setHeroHp(int value);

    void setEnemyHp(int value);

    void setTime(int value);

    void setRoundTime(int value);

    void setThreshold(int value);

    void changeThreshold(int value);

    void changeHeroStatus(Status status);

    void showResultOfAction(ArrayList<String> results);

    void startNewRound();
}
