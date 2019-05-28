package com.example.pusika.scrum.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.pusika.scrum.model.Cell;

public interface Playground {
    void findDecor();

    void createPoster(Bundle actors);

    void setImageToButton(View view, int resource);

    void createListeners();

    void placeDecor();

    void changeFrame();

    double getTimeProgressBarValue();

    void setTimeProgressBarValue(double value);

    double getRoundTimeProgressBarValue();

    void setRoundTimeProgressBarValue(double value);

    double getProtagonistHpProgressBarValue();

    void setProtagonistHpProgressBarValue(double value);

    double getAntagonistHpProgressBarValue();

    void setAntagonistHpProgressBarValue(double value);

    View findCell(int idOfCell);

    int getSizeOfCellArray();

    void setProtagonistHpTextView(String Hp);

    void makeAnimationOfChangedView(View view, int color);

    Cell[] getCells();

    void setIcon(ImageView imageView, String iconName);

    ImageView getHeroIcon();

    ImageView getEnemyIcon();

    String getBaseHeroIconName();

    String getBaseEnemyIconName();
}
