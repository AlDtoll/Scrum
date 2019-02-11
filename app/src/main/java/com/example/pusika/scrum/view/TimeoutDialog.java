package com.example.pusika.scrum.view;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pusika.scrum.R;
import com.example.pusika.scrum.model.ConditionOf;
import com.example.pusika.scrum.model.EffectOfAction;
import com.example.pusika.scrum.model.ScrumAction;
import com.example.pusika.scrum.model.Status;

import java.util.ArrayList;

import static com.example.pusika.scrum.view.MainActivity.HERO_STATUSES;
import static com.example.pusika.scrum.view.MainActivity.RESULT_OF_ROUND;
import static com.example.pusika.scrum.view.StartActivity.AUTO_ACTIONS;
import static com.example.pusika.scrum.view.StartActivity.HERO_ACTIONS;

public class TimeoutDialog extends DialogFragment implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    Stuntman stuntman;
    /**
     * Блок с вьюхами
     */
    LinearLayout listOfAction;
    TextView infoAboutRoundTextView;
    ArrayList<ScrumAction> scrumHeroActions = new ArrayList<>();
    ArrayList<ScrumAction> scrumAutoActions = new ArrayList<>();
    /**
     * Отдать
     */
    //todo возможно стоит сделать еще и с картинками
    ArrayList<String> resultOfActions = new ArrayList<>();
    private boolean isClicked = false;
    private ArrayList<Status> heroStatuses = new ArrayList<>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            stuntman = (Stuntman) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Stuntman");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        heroStatuses = (ArrayList) getArguments().getSerializable(HERO_STATUSES);
        scrumHeroActions = (ArrayList) getArguments().getSerializable(HERO_ACTIONS);
        scrumAutoActions = (ArrayList) getArguments().getSerializable(AUTO_ACTIONS);
        getDialog().setTitle("Раунд");

        //todo переделать timeout_dialog в recyclerView
        View v = inflater.inflate(R.layout.timeout_dialog, null);
        listOfAction = v.findViewById(R.id.listOfAction);
        infoAboutRoundTextView = v.findViewById(R.id.infoAboutRoundTextView);
        infoAboutRoundTextView.setText(getArguments().getString(RESULT_OF_ROUND));
        createTimeoutDialogActions();
        createTimeoutDialogActionsButton(inflater);

        return v;
    }

    private void createTimeoutDialogActions() {
        //todo нужен ли блок по умолчанию?
        //todo должен ли блок автодействий быть задан программно?
    }

    private void createTimeoutDialogActionsButton(LayoutInflater inflater) {
        int i = 0;
        for (ScrumAction scrumAction : scrumHeroActions) {
            //todo сделать свое view
            View timeoutDialogActionButton = inflater.inflate(R.layout.timeout_dialog_action_button, null);
            ImageView timeoutDialogActionButtonIcon = timeoutDialogActionButton.findViewById(R.id.iconOfAction);
            timeoutDialogActionButtonIcon.setImageResource(scrumAction.getIconOfAction());
            TextView timeoutDialogActionButtonName = timeoutDialogActionButton.findViewById(R.id.nameOfAction);
            timeoutDialogActionButtonName.setText(scrumAction.getNameOfAction());
            TextView timeoutDialogActionButtonDescription = timeoutDialogActionButton.findViewById(R.id.descriptionOfAction);
            timeoutDialogActionButtonDescription.setText(scrumAction.getDescriptionOfAction());
            timeoutDialogActionButton.setId(i);
            timeoutDialogActionButton.setOnClickListener(this);
            if (ConditionOf.isCondition(scrumAction.getConditionsOfAction(), heroStatuses)) {
                listOfAction.addView(timeoutDialogActionButton);
            }
            i++;
        }

    }

    public void onClick(View v) {
        //todo подтверждение
        isClicked = true;
        Log.d(LOG_TAG, "Dialog: " + v.getId());
        int numberOfAction = v.getId();
        executeSelectedAction(numberOfAction);
        executeAutoActions();
        dismiss();
    }

    private void executeSelectedAction(int numberOfAction) {
        ArrayList<EffectOfAction> effectsOfAction = scrumHeroActions.get(numberOfAction).getEffectsOfAction();
        executeAction(effectsOfAction);
    }

    private void executeAutoActions() {
        for (ScrumAction scrumAction : scrumAutoActions) {
            executeAction(scrumAction.getEffectsOfAction());
        }
    }

    private void executeAction(ArrayList<EffectOfAction> effectsOfAction) {
        for (EffectOfAction effectOfAction : effectsOfAction) {
            if (ConditionOf.isCondition(effectOfAction.getConditionsOfEffect(), heroStatuses)) {
                switch (effectOfAction.getEffect()) {
                    case CHANGE_HERO_HP:
                        stuntman.changeHeroHp(effectOfAction.getValue());
                        break;
                    case CHANGE_HERO_STATUS:
                        stuntman.changeHeroStatus(effectOfAction.getStatus());
                        break;
                    case CHANGE_ENEMY_HP:
                        stuntman.changeEnemyHp(effectOfAction.getValue());
                        break;
                    case CHANGE_TIME:
                        stuntman.changeTime(effectOfAction.getValue());
                        break;
                    case CHANGE_ROUND_TIME:
                        stuntman.changeRoundTime(effectOfAction.getValue());
                        break;
                    default:
                        throw new UnsupportedOperationException("в настоящий момент такой эффект не поддерживается");
                }
                resultOfActions.add(effectOfAction.getSuccess());
            } else {
                resultOfActions.add(effectOfAction.getFail());
            }
        }
    }

    public void onDismiss(DialogInterface dialog) {
        //todo предупреждение
        super.onDismiss(dialog);
        if (!isClicked) {
            resultOfActions.add("Герой выбрал... Бездействовать");
        }
        stuntman.showResultOfAction(resultOfActions);
        Log.d(LOG_TAG, "Dialog : onDismiss");

    }

    public void onCancel(DialogInterface dialog) {
        //todo предупреждение
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog : onCancel");
    }
}
