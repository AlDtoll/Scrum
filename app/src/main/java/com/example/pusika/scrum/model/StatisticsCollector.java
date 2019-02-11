package com.example.pusika.scrum.model;

import java.util.ArrayList;

public class StatisticsCollector {

    private ArrayList<Double> timesOfReaction = new ArrayList<>();

    public void takeTimeOfReaction(double timeOfReaction) {
        timesOfReaction.add(timeOfReaction);
    }
}
