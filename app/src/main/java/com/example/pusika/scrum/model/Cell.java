package com.example.pusika.scrum.model;

/**
 * Ячейка поля для битвы
 */
public class Cell {

    /**
     * Ничего нет
     */
    public final static int NONE = 0;

    //todo заменить на EnumSet
    /**
     * Ячейка, имеющая значение
     */
    public final static int SWORD_OR_SHIElD = 1;
    private int icon;

    public Cell(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
