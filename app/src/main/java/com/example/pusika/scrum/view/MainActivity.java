package com.example.pusika.scrum.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pusika.scrum.R;
import com.example.pusika.scrum.common.MyProgressBar;
import com.example.pusika.scrum.model.Cell;
import com.example.pusika.scrum.presenter.DirectorPresenter;
import com.example.pusika.scrum.view.common.BitmapHexagonDrawable;

import java.text.DecimalFormat;

//TODO разбить на MVP
//TODO покрыть тестами
//TODO сохранение статусов героя в базу, чтобы не пихать каждый раз в xml
//TODO все равно нужен какой-то способ получать информацию о результате сражения
//TODO сбор статистики, с тем чтобы подстраивать сложность под человека
//TODO снова покрыть тестами xD
//TODO странная альбомная ориентация?

public class MainActivity extends AppCompatActivity implements Playground {

    private final static long MAX_DRAWABLE_VALUE = 10000;
    private final static double ONE_PERCENT = MAX_DRAWABLE_VALUE / 100;
    /**
     * Блок с настройками для отображения поля
     */
    //todo это все тоже вынести в настройки читаемые снаружи
    private final static double ROUND_TIME_PROGRESS_BAR_MAX = 5000;
    private final static double TIME_PROGRESS_BAR_MAX = 2000;
    private final static int ROWS = 7;
    private final static int COLS = 7;
    final String TAG = "Log";
    int sizeOfCellArray = 0;

    /**
     * Блок с вьюхами
     */
    FrameLayout field;
    LinearLayout scale;
    MyProgressBar timeProgressBar;
    TextView timeTextView;
    MyProgressBar roundTimeProgressBar;
    TextView roundTimeTextView;
    MyProgressBar protagonistHpProgressBar;
    TextView protagonistHpTextView;
    ImageView iconOfHero;
    MyProgressBar antagonistHpProgressBar;
    TextView antagonistHpTextView;
    ImageView iconOfEnemy;
    View.OnClickListener onClickListener;
    DirectorPresenter directorPresenter = new DirectorPresenter();
    /**
     * Блок с полями для отображения
     */
    private String baseEnemyIconName;

    /**
     * Блок с полями для битвы
     */
    private Cell[] cells = new Cell[ROWS * COLS];
    private String baseHeroIconName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        directorPresenter.attachView(this);
        directorPresenter.attachContext(MainActivity.this);
        directorPresenter.createListeners();
        directorPresenter.prepareScene();
        directorPresenter.prepareActors();
        directorPresenter.motor();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop()");
        directorPresenter.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause()");
        directorPresenter.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onResume(). isPause ");
        directorPresenter.createStartBattleDialog();
    }

    @Override
    public void findDecor() {
        field = findViewById(R.id.field);
        scale = findViewById(R.id.scale);
        timeProgressBar = findViewById(R.id.timeProgressBar);
        timeTextView = findViewById(R.id.timeTextView);
        roundTimeProgressBar = findViewById(R.id.roundTimeProgressBar);
        roundTimeTextView = findViewById(R.id.roundTimeTextView);

        protagonistHpProgressBar = findViewById(R.id.protagonistHpProgressBar);
        protagonistHpTextView = findViewById(R.id.protagonistHpTextView);
        iconOfHero = findViewById(R.id.iconOfHero);

        antagonistHpProgressBar = findViewById(R.id.antagonistHpProgressBar);
        antagonistHpTextView = findViewById(R.id.antagonistHpTextView);
        iconOfEnemy = findViewById(R.id.iconOfEnemy);
    }

    @Override
    public void createPoster(Bundle actors) {
        iconOfHero.setImageResource(directorPresenter.getScene().getHero().getIcon());
        iconOfEnemy.setImageResource(directorPresenter.getScene().getEnemy().getIcon());

        baseHeroIconName = getResources().getResourceEntryName(directorPresenter.getScene().getHero().getIcon());
        baseEnemyIconName = getResources().getResourceEntryName(directorPresenter.getScene().getEnemy().getIcon());
    }

    public void createListeners() {
        onClickListener = view -> directorPresenter.onClick(view);
    }

    /**
     * Технический метод
     *
     * @param view кнопка паузы
     */
    public void pause(View view) {
        if (directorPresenter.isPause()) {
            directorPresenter.play();
        } else {
            directorPresenter.stop();
        }
    }

    @Override
    public void setImageToButton(View view, int resource) {
        ImageView button = (ImageView) view;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resource);
        Drawable dr = new BitmapHexagonDrawable(bitmap);
        button.setImageDrawable(dr);
    }

    @Override
    public View findCell(int idOfCell) {
        return findViewById(idOfCell);
    }

    /**
     * Метод, который создает игровое поле
     */
    //todo буэээ, заменить на что-то потребное
    @Override
    public void placeDecor() {
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int cols = COLS;
        int sizeOfCell = (int) (size.x / (cols + 0.5));
        Log.d(TAG, size.y / sizeOfCell - 1 + " клеток может быть в столбце");
        scale.setMinimumHeight(sizeOfCell);
        int rows;
        int counter = 0;
        int cellsWasCreated = 0;
        for (int i = 0; i < cols; i++) {
            if (i % 2 != 0) {
                rows = ROWS;
            } else {
                rows = ROWS - 1;
            }
            int cellWasInRow = 0;
            for (int j = 0; j < rows; j++) {
                cells[cellsWasCreated + j] = new Cell(Cell.NONE);
                Log.d(TAG, cellsWasCreated + j + " инициализирована");
                sizeOfCellArray++;
                cellWasInRow++;
                ImageView button = new ImageView(this);
                setImageToButton(button, R.drawable.none);
                button.setId(counter);
                button.setOnClickListener(onClickListener);
                counter++;
                ConstraintLayout.LayoutParams buttonLayoutParams = new ConstraintLayout.LayoutParams(sizeOfCell, sizeOfCell);
                int someMagicConstant = 24;
                buttonLayoutParams.leftMargin = someMagicConstant / 2 + (sizeOfCell) * i;
                if (i % 2 != 0) {
                    buttonLayoutParams.topMargin = someMagicConstant + (sizeOfCell) * j;
                } else {
                    buttonLayoutParams.topMargin = someMagicConstant + (sizeOfCell) * j + (sizeOfCell) / 2;
                }
                buttonLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
                buttonLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                constraintLayout.addView(button, buttonLayoutParams);
            }
            cellsWasCreated += cellWasInRow;
        }
        Log.d(TAG, sizeOfCellArray + "клеток");
        field.addView(constraintLayout, layoutParams);
    }

    @Override
    public void changeFrame() {
        double currentRoundTimeProgressBarValue = roundTimeProgressBar.getProgress();
        roundTimeProgressBar.setProgress(currentRoundTimeProgressBarValue + ONE_PERCENT);
        String currentRoundTime = new DecimalFormat("#0.00 с").format(currentRoundTimeProgressBarValue / MAX_DRAWABLE_VALUE * ROUND_TIME_PROGRESS_BAR_MAX / 1000.0);
        roundTimeTextView.setText(currentRoundTime);

        double currentTimeProgressBarValue = timeProgressBar.getProgress();
        timeProgressBar.setProgress(currentTimeProgressBarValue - ONE_PERCENT * ROUND_TIME_PROGRESS_BAR_MAX / TIME_PROGRESS_BAR_MAX);
        String currentTime = new DecimalFormat("#0.00 с").format(currentTimeProgressBarValue / MAX_DRAWABLE_VALUE * TIME_PROGRESS_BAR_MAX / 1000.0);
        timeTextView.setText(currentTime);

    }

    @Override
    public double getTimeProgressBarValue() {
        return timeProgressBar.getProgress();
    }

    @Override
    public void setTimeProgressBarValue(double value) {
        timeProgressBar.setProgress(value);
    }

    @Override
    public double getRoundTimeProgressBarValue() {
        return roundTimeProgressBar.getProgress();
    }

    @Override
    public void setRoundTimeProgressBarValue(double value) {
        roundTimeProgressBar.setProgress(value);
    }

    @Override
    public void setProtagonistHpTextView(String Hp) {
        protagonistHpTextView.setText(Hp);
    }

    @Override
    public double getProtagonistHpProgressBarValue() {
        return protagonistHpProgressBar.getProgress();
    }

    @Override
    public void setProtagonistHpProgressBarValue(double value) {
        protagonistHpProgressBar.setProgress(value);
    }

    @Override
    public double getAntagonistHpProgressBarValue() {
        return antagonistHpProgressBar.getProgress();
    }

    @Override
    public void setAntagonistHpProgressBarValue(double value) {
        antagonistHpProgressBar.setProgress(value);
    }

    @Override
    public int getSizeOfCellArray() {
        return sizeOfCellArray;
    }

    @Override
    public Cell[] getCells() {
        return cells;
    }

    @Override
    public void makeAnimationOfChangedView(View view, int color) {
        //todo механизм видимости шкалы в зависимости от настроек битвы
        if (view.getVisibility() != View.INVISIBLE) {
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.vibrate));
        }
        final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(((ViewGroup) view.getParent()),
                "backgroundColor",
                new ArgbEvaluator(),
                color,
                Color.WHITE);
        backgroundColorAnimator.setDuration(300);
        backgroundColorAnimator.start();
    }

    @Override
    public void setIcon(ImageView imageView, String iconName) {
        imageView.setImageResource(getBaseContext().getResources().getIdentifier(iconName, "drawable", getBaseContext().getPackageName()));
    }

    @Override
    public ImageView getHeroIcon() {
        return iconOfHero;
    }

    @Override
    public ImageView getEnemyIcon() {
        return iconOfEnemy;
    }

    @Override
    public String getBaseHeroIconName() {
        return baseHeroIconName;
    }

    @Override
    public String getBaseEnemyIconName() {
        return baseEnemyIconName;
    }
}
