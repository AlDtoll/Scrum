package com.example.pusika.scrum.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable {

    private String name;
    private int icon;
    private String description;
    private ArrayList<Status> statuses;

    public Place() {
    }

    public Place(String name, int icon, String description, ArrayList<Status> statuses) {
        this.name = name;
        this.icon = icon;
        this.description = description;
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

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }
}
