package com.example.pusika.scrum.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Шкала, которая меняет цвет в зависимости от значения и позволяет не париться с нецелочисленными значениями
 */
public class MyProgressBar extends android.support.v7.widget.AppCompatImageView {
    private final static int MAX_DRAWABLE_VALUE = 10000;

    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public double getProgress() {
        return this.getDrawable().getLevel();
    }

    public void setProgress(double value) {
        if (value > MAX_DRAWABLE_VALUE) {
            this.getDrawable().setLevel(MAX_DRAWABLE_VALUE);
        } else if (value < 0) {
            this.getDrawable().setLevel(0);
        } else {
            this.getDrawable().setLevel((int) value);
        }

    }
}
