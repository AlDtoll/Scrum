package com.example.pusika.scrum.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
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
import com.example.pusika.scrum.common.enums.HpStateEnum;
import com.example.pusika.scrum.model.Battle;
import com.example.pusika.scrum.model.Cell;
import com.example.pusika.scrum.model.Fighter;
import com.example.pusika.scrum.model.Hero;
import com.example.pusika.scrum.model.Place;
import com.example.pusika.scrum.model.ScrumAction;
import com.example.pusika.scrum.model.StatisticsCollector;
import com.example.pusika.scrum.model.Status;
import com.example.pusika.scrum.view.common.BitmapHexagonDrawable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.pusika.scrum.view.StartActivity.AUTO_ACTIONS;
import static com.example.pusika.scrum.view.StartActivity.HERO_ACTIONS;

//TODO разбить на MVP
//TODO покрыть тестами
//TODO сохранение статусов героя в базу, чтобы не пихать каждый раз в xml
//TODO все равно нужен какой-то способ получать информацию о результате сражения
//TODO сбор статистики, с тем чтобы подстраивать сложность под человека
//TODO снова покрыть тестами xD
//TODO странная альбомная ориентация?

public class MainActivity extends AppCompatActivity implements Stuntman {

    public final static String RESULT_OF_ROUND = "resultOfRound";
    public final static String HERO_STATUSES = "hero_statuses";
    public final static String RESULT_OF_ACTION = "resultOfAction";
    private final static long MAX_DRAWABLE_VALUE = 10000;
    private final static double ONE_PERCENT = MAX_DRAWABLE_VALUE / 100;
    /**
     * Блок с настройками для отображения поля
     */
    //todo это все тоже вынести в настройки читаемые снаружи
    private final static double ROUND_TIME_PROGRESS_BAR_MAX = 5000;
    private final static double TIME_PROGRESS_BAR_MAX = 2000;
    private final static double POINT_IN_ONE_MILLISECONDS = MAX_DRAWABLE_VALUE / TIME_PROGRESS_BAR_MAX;
    private final static int ROWS = 7;
    private final static int COLS = 7;
    private final static double TIME_OF_ANIMATION = 200;
    final String TAG = "Log";
    boolean isAttack = true;
    boolean isTheEndOfRound = false;
    boolean isPause = true;
    int idOfActiveCell;
    int sizeOfCellArray = 0;
    int idOfClickedCell;
    Hero hero;
    Fighter enemy;
    Battle battle;
    int timeAfterAttack;
    int timeAfterDefence;
    int timeAfterGetHit;
    int threshold;
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
    /**
     * Блок с полями для отображения
     */
    private Timer timer = new Timer();
    private String baseEnemyIcon;
    private double pointInOneHpPoint;
    private int animationValue = 0;
    private int previousHeroHp = 0;
    private int previousEnemyHp = 0;
    /**
     * Блок с полями для битвы
     */
    private Cell[] cells = new Cell[ROWS * COLS];
    private Place place;
    private ArrayList<ScrumAction> scrumHeroActions = new ArrayList<>();
    private ArrayList<ScrumAction> scrumAutoActions = new ArrayList<>();
    /**
     * Блок для сбора статистики
     */
    private StatisticsCollector statisticsCollector = new StatisticsCollector();
    private double timeOfReaction = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //todo состряпать билдер?
        findComponentsById();
        createBattle();
        createListeners();
        initField();
        startFight();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop(). isPause " + isPause);
        isPause = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause(). isPause " + isPause);
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onResume(). isPause " + isPause);
        createStartBattleDialog();
    }

    private void createStartBattleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        StringBuilder message = new StringBuilder();
        message.append(hero.getName())
                .append(" против ")
                .append(enemy.getName());
        builder
                .setTitle("БОЙ!")
                .setMessage(message.toString())
                .setPositiveButton("Ok", (dialog, which) -> isPause = false)
                .create();
        builder.show();
    }

    private void findComponentsById() {
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

    private void createBattle() {
        Bundle arguments = getIntent().getExtras();
        place = (Place) arguments.get("place");
        hero = (Hero) arguments.get("hero");
        enemy = (Fighter) arguments.get("enemy");

        //todo убрать
        scrumHeroActions = (ArrayList<ScrumAction>) arguments.get(HERO_ACTIONS);
        scrumAutoActions = (ArrayList<ScrumAction>) arguments.get(AUTO_ACTIONS);

        battle = new Battle(hero, enemy);
        iconOfHero.setImageResource(hero.getIcon());
        iconOfEnemy.setImageResource(enemy.getIcon());
        baseEnemyIcon = getResources().getResourceEntryName(enemy.getIcon());

        timeAfterAttack = hero.getTimeAfterAttack();
        timeAfterDefence = hero.getTimeAfterDefence();
        timeAfterGetHit = hero.getTimeAfterGetHit();
        threshold = hero.getThreshold();

        pointInOneHpPoint = MAX_DRAWABLE_VALUE / hero.getMaxHp();
        //todo настраиваемое значение?
        timeProgressBar.setProgress(ONE_PERCENT * 100);
        protagonistHpProgressBar.setProgress(hero.getHp() * pointInOneHpPoint);
        antagonistHpProgressBar.setProgress(enemy.getHp() * pointInOneHpPoint);

    }

    private void createListeners() {
        onClickListener = view -> {
            clearButton(view);
            doAction(view);
        };
    }

    /**
     * Технический метод
     *
     * @param view кнопка паузы
     */
    public void pause(View view) {
        isPause = !isPause;
    }

    private void clearButton(View view) {
        ImageView imageButton = findViewById(view.getId());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.none);
        Drawable dr = new BitmapHexagonDrawable(bitmap);
        imageButton.setImageDrawable(dr);
    }

    private void doAction(View v) {
        executeAnimationOfClick(v);
        takeStatistic();
        shakeWeapon(v);

    }

    private void takeStatistic() {
        //todo тут собираем статистику по раунду
        statisticsCollector.takeTimeOfReaction(timeOfReaction);
        timeOfReaction = 0;
    }

    private void executeAnimationOfClick(View view) {
        idOfClickedCell = view.getId();

        ImageView clickedButton = (ImageView) view;
        Bitmap bitmap;
        if (view.getId() == idOfActiveCell) {
            if (isAttack) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.greensword);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.greenshield);
            }
            Drawable dr = new BitmapHexagonDrawable(bitmap);
            clickedButton.setImageDrawable(dr);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red);
            Drawable dr = new BitmapHexagonDrawable(bitmap);
            clickedButton.setImageDrawable(dr);
        }
        animationValue = (int) TIME_OF_ANIMATION;
    }

    private void shakeWeapon(View view) {
        double currentTimeProgressBarValue = timeProgressBar.getProgress();
        int numberOfCell = view.getId();
        Cell cell = cells[numberOfCell];
        switch (cell.getIcon()) {
            case Cell.NONE:
                makeMiss();
                break;
            case Cell.SWORD_OR_SHIElD:
                if (isAttack) {
                    battle.strike(hero.getDmg());
                    timeProgressBar.setProgress(currentTimeProgressBarValue + timeAfterAttack * POINT_IN_ONE_MILLISECONDS);
                } else {
                    battle.block();
                    timeProgressBar.setProgress(currentTimeProgressBarValue + timeAfterDefence * POINT_IN_ONE_MILLISECONDS);
                }
                cell.setIcon(Cell.NONE);
                changeIconPlace();
                break;
            default:
                break;
        }
        if (isTheEndOfRound) {
            finishRound();
        }
    }

    /**
     * Метод, который создает игровое поле
     */
    //todo буэээ, заменить на что-то потребное
    private void initField() {
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
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.none);
                Drawable dr = new BitmapHexagonDrawable(bitmap);
                button.setImageDrawable(dr);
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

    private void finishRound() {
        TimeoutDialog timeoutDialog = new TimeoutDialog();
        Bundle args = new Bundle();
        args.putSerializable(HERO_STATUSES, hero.getStatuses());
        //todo убрать после прямого прокидывания
        args.putSerializable(HERO_ACTIONS, scrumHeroActions);
        args.putSerializable(AUTO_ACTIONS, scrumAutoActions);
        createResultOfRound(args);

        timeoutDialog.setArguments(args);
        timeoutDialog.show(getFragmentManager(), "timeoutDialog");
        isPause = true;
    }

    private void createResultOfRound(Bundle args) {
        //todo нужно ли это все здесь? или только в конце?
        StringBuilder message = new StringBuilder();
        message.append("За раунд герой нанес ").append(battle.getHits()).append(" ударов, ")
                .append("\n")
                .append("промахнулся ").append(battle.getMisses()).append(" раз")
                .append("\n")
                .append("заблокировал ").append(battle.getBlocks()).append(" ударов")
                .append("\n")
                .append(" и получил ").append(battle.getCuts()).append(".")
                .append("\n")
                .append("Похоже, что противник ").append(checkFighterHp(enemy).getCode());

        args.putString(RESULT_OF_ROUND, String.valueOf(message));
    }

    private HpStateEnum checkFighterHp(Fighter fighter) {
        if (fighter.getHp() == fighter.getMaxHp()) {
            return HpStateEnum.HEALTHY;
        }
        if (fighter.getHp() == fighter.getMaxHp() - 1) {
            return HpStateEnum.SCRATCH;
        }
        if (fighter.getHp() >= fighter.getMaxHp() / 2) {
            return HpStateEnum.CUT;
        }
        if (fighter.getHp() < fighter.getMaxHp() / 2 && fighter.getHp() != 0) {
            return HpStateEnum.BAD;
        }
        return HpStateEnum.DEAD;
    }

    private void makeMiss() {
        // todo какой эффект для боя. changeIconPlace() например
        battle.miss();
    }

    private void startFight() {
        changeIconPlace();
        final ImageView[] imageButton = new ImageView[1];
        final ImageView[] clickedButton = new ImageView[1];
        final double[] currentRoundTimeProgressBarValue = new double[1];
        final String[] currentRoundTime = new String[1];
        final double[] currentTimeProgressBarValue = new double[1];
        final String[] currentTime = new String[1];
        final String[] currentHeroLife = new String[1];
        final Bitmap[] bitmap = new Bitmap[1];
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (!isPause) {
                        currentRoundTimeProgressBarValue[0] = roundTimeProgressBar.getProgress();
                        roundTimeProgressBar.setProgress(currentRoundTimeProgressBarValue[0] + ONE_PERCENT);
                        currentRoundTime[0] = new DecimalFormat("#0.00 с").format(currentRoundTimeProgressBarValue[0] / MAX_DRAWABLE_VALUE * ROUND_TIME_PROGRESS_BAR_MAX / 1000.0);
                        roundTimeTextView.setText(currentRoundTime[0]);

                        if (roundTimeProgressBar.getProgress() == MAX_DRAWABLE_VALUE) {
                            isTheEndOfRound = true;
                            if (timeProgressBar.getProgress() == 0) {
                                finishRound();
                            }
                        }
                        currentTimeProgressBarValue[0] = timeProgressBar.getProgress();
                        timeProgressBar.setProgress(currentTimeProgressBarValue[0] - ONE_PERCENT * ROUND_TIME_PROGRESS_BAR_MAX / TIME_PROGRESS_BAR_MAX);
                        currentTime[0] = new DecimalFormat("#0.00 с").format(currentTimeProgressBarValue[0] / MAX_DRAWABLE_VALUE * TIME_PROGRESS_BAR_MAX / 1000.0);
                        timeTextView.setText(currentTime[0]);

                        if (currentTimeProgressBarValue[0] == 0) {
                            battle.getHit(enemy.getDmg());

                            timeProgressBar.setProgress(currentTimeProgressBarValue[0] + timeAfterGetHit * POINT_IN_ONE_MILLISECONDS);
                        }


                        if (currentTimeProgressBarValue[0] <= threshold * POINT_IN_ONE_MILLISECONDS) {
                            isAttack = false;
                        } else {
                            isAttack = true;
                        }

                        if (isAttack) {
                            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.sword);
                        } else {
                            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.shield);
                        }
                        Drawable dr = new BitmapHexagonDrawable(bitmap[0]);
                        imageButton[0] = findViewById(idOfActiveCell);
                        imageButton[0].setImageDrawable(dr);

                        if (animationValue == 0) {
                            if (idOfClickedCell < sizeOfCellArray) {
                                clickedButton[0] = findViewById(idOfClickedCell);
                                clearButton(clickedButton[0]);
                            }
                            idOfClickedCell = sizeOfCellArray + 1;
                            field.setBackgroundColor(Color.WHITE);
                        }
                        animationValue = animationValue - 100;

                        protagonistHpProgressBar.setProgress(battle.getHeroHp() * pointInOneHpPoint);
                        currentHeroLife[0] = battle.getHeroHp() + " / " + hero.getMaxHp();
                        protagonistHpTextView.setText(currentHeroLife[0]);
                        antagonistHpProgressBar.setProgress(battle.getEnemyHp() * pointInOneHpPoint);

                        if (protagonistHpProgressBar.getProgress() == 0) {
                            defeat();
                        }

                        if (antagonistHpProgressBar.getProgress() == 0) {
                            victory();
                        }
                        makeBlinks();

                        timeOfReaction++;
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, (long) ROUND_TIME_PROGRESS_BAR_MAX / 100);
    }

    private void changeIconPlace() {
        Random random = new Random(new Date().getTime());
        int numberOfCell = random.nextInt(sizeOfCellArray - 1);
        Log.d(TAG, numberOfCell + "клетка");
        idOfActiveCell = numberOfCell;
        cells[numberOfCell].setIcon(Cell.SWORD_OR_SHIElD);
        clearButton(findViewById(numberOfCell));
    }

    private void makeBlinks() {
        if (previousHeroHp != battle.getHeroHp()) {
            makeAnimationOfChangedView(protagonistHpProgressBar, previousHeroHp > battle.getHeroHp() ? Color.RED : Color.GREEN);

            changeFighterIcon(hero);
        }
        previousHeroHp = battle.getHeroHp();
        if (previousEnemyHp != battle.getEnemyHp()) {
            makeAnimationOfChangedView(antagonistHpProgressBar, previousEnemyHp > battle.getEnemyHp() ? Color.RED : Color.GREEN);

            changeFighterIcon(enemy);
        }
        previousEnemyHp = battle.getEnemyHp();
    }

    private void makeAnimationOfChangedView(View view, int color) {
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

    private void changeFighterIcon(Fighter fighter) {
        if (fighter.getName().equals(hero.getName())) {
            switch (checkFighterHp(hero)) {
                case HEALTHY:
                    iconOfHero.setImageResource(getBaseContext().getResources().getIdentifier("hero", "drawable", getBaseContext().getPackageName()));
                    break;
                case CUT:
                    iconOfHero.setImageResource(getBaseContext().getResources().getIdentifier("hero_cut", "drawable", getBaseContext().getPackageName()));
                    break;
                case SCRATCH:
                    iconOfHero.setImageResource(getBaseContext().getResources().getIdentifier("hero_scratch", "drawable", getBaseContext().getPackageName()));
                    break;
                case BAD:
                    iconOfHero.setImageResource(getBaseContext().getResources().getIdentifier("hero_bad", "drawable", getBaseContext().getPackageName()));
                    break;
                case DEAD:
                    iconOfHero.setImageResource(getBaseContext().getResources().getIdentifier("grave", "drawable", getBaseContext().getPackageName()));
                    break;
            }
        } else {
            switch (checkFighterHp(enemy)) {
                case HEALTHY:
                    iconOfEnemy.setImageResource(getBaseContext().getResources().getIdentifier(baseEnemyIcon, "drawable", getBaseContext().getPackageName()));
                    break;
                case CUT:
                    iconOfEnemy.setImageResource(getBaseContext().getResources().getIdentifier(baseEnemyIcon + "_cut", "drawable", getBaseContext().getPackageName()));
                    break;
                case SCRATCH:
                    iconOfEnemy.setImageResource(getBaseContext().getResources().getIdentifier(baseEnemyIcon + "_scratch", "drawable", getBaseContext().getPackageName()));
                    break;
                case BAD:
                    iconOfEnemy.setImageResource(getBaseContext().getResources().getIdentifier(baseEnemyIcon + "_bad", "drawable", getBaseContext().getPackageName()));
                    break;
                case DEAD:
                    iconOfEnemy.setImageResource(getBaseContext().getResources().getIdentifier("grave", "drawable", getBaseContext().getPackageName()));
                    break;
            }
        }
    }

    private void victory() {

    }

    private void defeat() {
        //todo пораженние либо побег
    }

    //todo дефолтное поведение
    @Override
    public void changeHeroHp(int value) {
        battle.setHeroHp(battle.getHeroHp() + value);
    }

    @Override
    public void changeEnemyHp(int value) {
        battle.setEnemyHp(battle.getEnemyHp() + value);
    }

    @Override
    public void changeTime(int value) {

    }

    @Override
    public void changeRoundTime(int value) {

    }

    @Override
    public void setHeroHp(int value) {

    }

    @Override
    public void setEnemyHp(int value) {

    }

    @Override
    public void setTime(int value) {

    }

    @Override
    public void setRoundTime(int value) {

    }

    @Override
    public void setThreshold(int value) {

    }

    @Override
    public void changeThreshold(int value) {

    }

    @Override
    public void changeHeroStatus(Status statusForChange) {
        boolean isChanged = false;
        ArrayList<Status> statuses = hero.getStatuses();
        for (Status status : statuses) {
            if (status.getName().equals(statusForChange.getName())) {
                status.setValue(status.getValue() + statusForChange.getValue());
                isChanged = true;
            }
        }
        if (!isChanged) {
            statuses.add(statusForChange);
        }
    }

    @Override
    public void showResultOfAction(ArrayList<String> results) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        StringBuilder message = new StringBuilder();
        for (String result : results) {
            message
                    .append(result)
                    .append("\n");
        }
        builder
                .setTitle("В результате...")
                .setMessage(message.toString())
                .setPositiveButton("Ok", (dialog, which) -> {
                    isPause = false;
                    startNewRound();
                })
                //todo на отмену действие
                .create();
        builder.show();
    }

    @Override
    public void startNewRound() {
        roundTimeProgressBar.setProgress(0);
        timeProgressBar.setProgress(MAX_DRAWABLE_VALUE / 2);
        isTheEndOfRound = false;
        battle.startRound();
    }
}
