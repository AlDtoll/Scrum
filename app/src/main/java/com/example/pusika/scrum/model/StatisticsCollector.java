package com.example.pusika.scrum.model;

import java.io.Serializable;
import java.util.ArrayList;

public class StatisticsCollector implements Serializable {

    private ArrayList<Double> timesOfReaction = new ArrayList<>();
    private ArrayList<ResultOfRound> resultsOfRounds = new ArrayList<>();

    public void takeTimeOfReaction(double timeOfReaction) {
        timesOfReaction.add(timeOfReaction);
    }

    public ArrayList<Double> getTimesOfReaction() {
        return new ArrayList<>(timesOfReaction);
    }

    public ArrayList<ResultOfRound> getResultsOfRounds() {
        return new ArrayList<>(resultsOfRounds);
    }

    public void addResultOfRound(Scene battle) {
        resultsOfRounds.add(new ResultOfRound(
                battle.getHits(),
                battle.getMisses(),
                battle.getBlocks(),
                battle.getCuts(),
                battle.getHeroHp(),
                battle.getEnemyHp()
        ));
    }

    public double getAverageTimeOfReaction() {
        double averageTime = 0;
        for (Double timeOfReaction : timesOfReaction) {
            averageTime = averageTime + timeOfReaction;
        }
        return averageTime / timesOfReaction.size();
    }
}
