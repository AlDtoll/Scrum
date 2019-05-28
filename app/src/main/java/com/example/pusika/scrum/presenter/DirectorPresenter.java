package com.example.pusika.scrum.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.pusika.scrum.R;
import com.example.pusika.scrum.common.enums.HpStateEnum;
import com.example.pusika.scrum.model.Cell;
import com.example.pusika.scrum.model.Fighter;
import com.example.pusika.scrum.model.Scene;
import com.example.pusika.scrum.model.StatisticsCollector;
import com.example.pusika.scrum.model.Status;
import com.example.pusika.scrum.view.MainActivity;
import com.example.pusika.scrum.view.Playground;
import com.example.pusika.scrum.view.Stuntman;
import com.example.pusika.scrum.view.TimeoutDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DirectorPresenter implements Stuntman, Serializable {

    public final static String PRESENTER = "presenter";
    public final static String RESULT_OF_ROUND = "resultOfRound";
    private final static long MAX_DRAWABLE_VALUE = 10000;
    private final static double ROUND_TIME_PROGRESS_BAR_MAX = 5000;
    private final static double TIME_PROGRESS_BAR_MAX = 2000;
    private final static double POINT_IN_ONE_MILLISECONDS = MAX_DRAWABLE_VALUE / TIME_PROGRESS_BAR_MAX;
    private final static double TIME_OF_ANIMATION = 200;

    private boolean isPause = true;
    private Playground playground;
    private Scene scene = new Scene();
    private Context context;

    private int idOfClickedCell;
    private int idOfActiveCell;
    private boolean isAttack = true;
    private int animationValue = 0;
    private boolean isTheEndOfRound = false;
    private double pointInOneHpPoint;

    /**
     * Блок для сбора статистики
     */
    private StatisticsCollector statisticsCollector = new StatisticsCollector();
    private double timeOfReaction = 0;

    private int previousHeroHp = 0;
    private int previousEnemyHp = 0;

    public void stop() {
        isPause = true;
    }

    public void play() {
        isPause = false;
    }

    public boolean isPause() {
        return isPause;
    }

    public void attachView(Playground playground) {
        this.playground = playground;
    }

    public Playground getPlayground() {
        return playground;
    }

    public void prepareScene() {
        getPlayground().findDecor();
        getPlayground().placeDecor();
        //todo настраиваемое
        getPlayground().setTimeProgressBarValue(MAX_DRAWABLE_VALUE);
    }

    public Scene getScene() {
        return scene;
    }


    public void prepareActors() {
        Bundle scenario = ((Activity) getPlayground()).getIntent().getExtras();
        scene.readScenario(scenario);
        pointInOneHpPoint = MAX_DRAWABLE_VALUE / scene.getHero().getMaxHp();
        getPlayground().createPoster(scenario);
    }

    public void createStartBattleDialog() {
        String message = scene.getHero().getName() +
                " против " +
                scene.getEnemy().getName();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle("БОЙ!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> play())
                .create();
        builder.show();
    }

    public void onClick(View v) {
        executeAnimationOfClick(v);
        takeStatistic();
        shakeWeapon(v);
    }

    private void executeAnimationOfClick(View view) {
        idOfClickedCell = view.getId();
        if (view.getId() == idOfActiveCell) {
            if (isAttack) {
                playground.setImageToButton(view, R.drawable.greensword);
            } else {
                playground.setImageToButton(view, R.drawable.greenshield);
            }
        } else {
            playground.setImageToButton(view, R.drawable.red);
        }
        animationValue = (int) TIME_OF_ANIMATION;
    }

    private void shakeWeapon(View view) {
        double currentTimeProgressBarValue = getPlayground().getTimeProgressBarValue();
        int numberOfCell = view.getId();
        Cell cell = getPlayground().getCells()[numberOfCell];
        switch (cell.getIcon()) {
            case Cell.NONE:
                makeMiss();
                break;
            case Cell.SWORD_OR_SHIElD:
                if (isAttack) {
                    scene.strike(scene.getHero().getDmg());
                    getPlayground().setTimeProgressBarValue(currentTimeProgressBarValue + scene.getHero().getTimeAfterAttack() * POINT_IN_ONE_MILLISECONDS);
                } else {
                    scene.block();
                    getPlayground().setTimeProgressBarValue(currentTimeProgressBarValue + scene.getHero().getTimeAfterAttack() * POINT_IN_ONE_MILLISECONDS);
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

    private void takeStatistic() {
        statisticsCollector.takeTimeOfReaction(timeOfReaction);
        timeOfReaction = 0;
    }

    private void changeIconPlace() {
        Random random = new Random(new Date().getTime());
        int numberOfCell = random.nextInt(getPlayground().getSizeOfCellArray() - 1);
        idOfActiveCell = numberOfCell;
        getPlayground().getCells()[numberOfCell].setIcon(Cell.SWORD_OR_SHIElD);
        getPlayground().setImageToButton(getPlayground().findCell(numberOfCell), R.drawable.none);
    }

    private void makeMiss() {

    }

    public void createListeners() {
        playground.createListeners();
    }

    public void motor() {
        changeIconPlace();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                ((Activity) getPlayground()).runOnUiThread(() -> createRails());
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, (long) ROUND_TIME_PROGRESS_BAR_MAX / 100);
    }

    private void createRails() {
        if (!isPause) {
            playground.changeFrame();
            if (playground.getRoundTimeProgressBarValue() == MAX_DRAWABLE_VALUE) {
                isTheEndOfRound = true;
            }
            if (playground.getTimeProgressBarValue() == 0) {
                timeProgressBarIsEmpty();
            }

            if (playground.getTimeProgressBarValue() <= scene.getHero().getThreshold() * POINT_IN_ONE_MILLISECONDS) {
                isAttack = false;
            } else {
                isAttack = true;
            }
            View activeCell = getPlayground().findCell(idOfActiveCell);
            getPlayground().setImageToButton(activeCell, isAttack ? R.drawable.sword : R.drawable.shield);

            if (animationValue == 0) {
                if (idOfClickedCell < getPlayground().getSizeOfCellArray()) {
                    View clickedCell = getPlayground().findCell(idOfClickedCell);
                    getPlayground().setImageToButton(clickedCell, R.drawable.none);
                }
                idOfClickedCell = getPlayground().getSizeOfCellArray() + 1;
            }
            animationValue = animationValue - 100;

            getPlayground().setProtagonistHpProgressBarValue(scene.getHeroHp() * pointInOneHpPoint);
            getPlayground().setProtagonistHpTextView(scene.getHeroHp() + " / " + scene.getHero().getMaxHp());
            getPlayground().setAntagonistHpProgressBarValue(scene.getEnemyHp() * pointInOneHpPoint);

            makeBlinks();

            if (getPlayground().getProtagonistHpProgressBarValue() == 0) {
                defeat();
            }

            if (getPlayground().getAntagonistHpProgressBarValue() == 0) {
                victory();
            }

            timeOfReaction++;
        }
    }

    private void makeBlinks() {
        //todo перенести в свойство самого героя
        previousHeroHp = makeBlink(previousHeroHp, R.id.protagonistHpProgressBar, scene.getHero());
        previousEnemyHp = makeBlink(previousEnemyHp, R.id.antagonistHpProgressBar, scene.getEnemy());
    }

    private int makeBlink(int previousHp, int idOfView, Fighter fighter) {
        if (previousHp != fighter.getHp()) {
            getPlayground().makeAnimationOfChangedView(((Activity) getPlayground()).findViewById(idOfView), previousHp > fighter.getHp() ? Color.RED : Color.GREEN);
            changeFighterIcon(fighter);
            previousHp = fighter.getHp();
        }
        return previousHp;
    }

    private void changeFighterIcon(Fighter fighter) {
        if (fighter.getName().equals(getScene().getHero().getName())) {
            selectIcon(getPlayground().getHeroIcon(), checkFighterHp(getScene().getHero()), getPlayground().getBaseHeroIconName());
        } else {
            selectIcon(getPlayground().getEnemyIcon(), checkFighterHp(getScene().getEnemy()), getPlayground().getBaseEnemyIconName());
        }
    }

    private void selectIcon(ImageView imageView, HpStateEnum hpState, String iconName) {
        switch (hpState) {
            case HEALTHY:
                getPlayground().setIcon(imageView, iconName);
                break;
            case CUT:
                getPlayground().setIcon(imageView, iconName + "_cut");
                break;
            case SCRATCH:
                getPlayground().setIcon(imageView, iconName + "_scratch");
                break;
            case BAD:
                getPlayground().setIcon(imageView, iconName + "_bad");
                break;
            case DEAD:
                getPlayground().setIcon(imageView, "grave");
                break;
        }
    }

    public HpStateEnum checkFighterHp(Fighter fighter) {
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

    private void victory() {

    }

    private void defeat() {

    }

    private void timeProgressBarIsEmpty() {
        if (isTheEndOfRound) {
            finishRound();
        } else {
            scene.getHit(scene.getEnemy().getDmg());
            getPlayground().setTimeProgressBarValue(scene.getHero().getTimeAfterGetHit() * POINT_IN_ONE_MILLISECONDS);
        }
    }

    private void finishRound() {
        if (!isPause) {
            TimeoutDialog timeoutDialog = new TimeoutDialog();
            Bundle args = new Bundle();
            args.putSerializable(PRESENTER, this);
            createResultOfRound(args);
            timeoutDialog.setArguments(args);
            timeoutDialog.show(((Activity) getPlayground()).getFragmentManager(), "timeoutDialog");
        }
        isPause = true;
    }

    private void createResultOfRound(Bundle args) {
        //todo нужно ли это все здесь? или только в конце?
        StringBuilder message = new StringBuilder();
        message.append("За раунд герой нанес ").append(scene.getHits()).append(" ударов, ")
                .append("\n")
                .append("промахнулся ").append(scene.getMisses()).append(" раз")
                .append("\n")
                .append("заблокировал ").append(scene.getBlocks()).append(" ударов")
                .append("\n")
                .append(" и получил ").append(scene.getCuts()).append(".")
                .append("\n")
                .append("Похоже, что противник ").append(checkFighterHp(scene.getEnemy()).getCode())
                .append("\n")
                .append("Герой:")
                .append(scene.getHero().getStatuses())
                .append("\n")
                .append("Противник:")
                .append(scene.getEnemy().getStatuses());

        args.putString(RESULT_OF_ROUND, String.valueOf(message));
    }

    public void showResultOfActions(ArrayList<String> resultOfActions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        StringBuilder message = new StringBuilder();
        for (String result : resultOfActions) {
            message
                    .append(result)
                    .append("\n");
        }
        builder
                .setTitle("В результате...")
                .setMessage(message.toString())
                .setPositiveButton("Ok", (dialog, which) -> startNewRound())
                //todo на отмену действие
                .create();
        builder.show();
    }

    public void startNewRound() {
        getPlayground().setTimeProgressBarValue(MAX_DRAWABLE_VALUE / 2);
        getPlayground().setRoundTimeProgressBarValue(0);
        getScene().startRound();
        isTheEndOfRound = false;
        isPause = false;
    }

    public void attachContext(MainActivity mainActivity) {
        context = mainActivity;
    }

    @Override
    public void changeHeroHp(int value) {
        getScene().setHeroHp(getScene().getHeroHp() + value);
    }

    @Override
    public void changeEnemyHp(int value) {
        getScene().setEnemyHp(getScene().getEnemyHp() + value);
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
        getScene().getHero().setThreshold(value);
    }

    @Override
    public void changeThreshold(int value) {
        getScene().getHero().setThreshold(getScene().getHero().getThreshold() + value);
    }

    @Override
    public void changeHeroStatus(Status statusForChange) {
        boolean isChanged = false;
        ArrayList<Status> statuses = getScene().getHero().getStatuses();
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
    public void changeEnemyStatus(Status statusForChange) {
        boolean isChanged = false;
        ArrayList<Status> statuses = getScene().getEnemy().getStatuses();
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
}
