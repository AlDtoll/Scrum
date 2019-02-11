package com.example.pusika.scrum.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.pusika.scrum.view.MainActivity.RESULT_OF_ACTION;

@Deprecated
public class ResultDialog extends DialogFragment implements DialogInterface.OnClickListener {

    Stuntman stuntman;

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

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> results = (ArrayList) getArguments().getSerializable(RESULT_OF_ACTION);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        StringBuilder message = new StringBuilder();
        for (String result : results) {
            message
                    .append(result)
                    .append("\n");
        }
        return builder
                .setTitle("В результате...")
                .setMessage(message.toString())
                .setPositiveButton("Ok", this)
                .create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                dismiss();
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
            case Dialog.BUTTON_NEUTRAL:

                break;
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        stuntman.startNewRound();
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        stuntman.startNewRound();
    }
}
