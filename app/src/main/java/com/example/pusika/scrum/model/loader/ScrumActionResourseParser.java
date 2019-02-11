package com.example.pusika.scrum.model.loader;

import android.content.Context;

import com.example.pusika.scrum.common.enums.EffectEnum;
import com.example.pusika.scrum.common.enums.ExpressionEnum;
import com.example.pusika.scrum.model.ConditionOf;
import com.example.pusika.scrum.model.EffectOfAction;
import com.example.pusika.scrum.model.ScrumAction;
import com.example.pusika.scrum.model.Status;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class ScrumActionResourseParser {
    private ArrayList<ScrumAction> scrumActions;
    private Context context;

    public ScrumActionResourseParser(Context context) {
        scrumActions = new ArrayList<>();
        this.context = context;
    }

    public ArrayList<ScrumAction> getActions() {
        return scrumActions;
    }

    public boolean parse(XmlPullParser xpp) {
        boolean isSuccess = true;
        boolean inEntry = false;
        boolean inEffects = false;
        boolean inStatus = false;
        boolean inConditions = false;
        ScrumAction scrumAction = null;
        String textValue = "";
        EffectOfAction effectOfAction = null;
        ArrayList<EffectOfAction> effectsOfAction = new ArrayList<>();
        Status status = null;
        ConditionOf conditionOf = null;
        ArrayList<ConditionOf> conditionsOf = new ArrayList<>();

        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("ScrumHeroActions".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            scrumActions = new ArrayList<>();
                        } else if ("ScrumAction".equalsIgnoreCase(tagName)) {
                            scrumAction = new ScrumAction();
                        } else if ("ScrumEffects".equalsIgnoreCase(tagName)) {
                            inEffects = true;
                        } else if ("ScrumEffect".equalsIgnoreCase(tagName)) {
                            effectOfAction = new EffectOfAction();
                        } else if ("ScrumStatus".equalsIgnoreCase(tagName)) {
                            status = new Status();
                            inStatus = true;
                        } else if ("ScrumConditions".equalsIgnoreCase(tagName)) {
                            inConditions = true;
                            conditionsOf = new ArrayList<>();
                        } else if ("ScrumCondition".equalsIgnoreCase(tagName)) {
                            conditionOf = new ConditionOf();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inConditions) {
                            if ("ScrumExpression".equalsIgnoreCase(tagName)) {
                                conditionOf.setExpression(ExpressionEnum.of(textValue));
                            } else if ("ScrumStatusName".equalsIgnoreCase(tagName)) {
                                conditionOf.setStatusName(textValue);
                            } else if ("ScrumValue".equalsIgnoreCase(tagName)) {
                                conditionOf.setValue(Integer.valueOf(textValue));
                            } else if ("ScrumCondition".equalsIgnoreCase(tagName)) {
                                conditionsOf.add(conditionOf);
                            } else if ("ScrumConditions".equalsIgnoreCase(tagName)) {
                                inConditions = false;
                            }
                        }
                        if (inEntry && inStatus && !inConditions) {
                            if ("ScrumName".equalsIgnoreCase(tagName)) {
                                status.setName(textValue);
                            } else if ("ScrumValue".equalsIgnoreCase(tagName)) {
                                status.setValue(Integer.valueOf(textValue));
                            } else if ("ScrumDescription".equalsIgnoreCase(tagName)) {
                                status.setDescription(textValue);
                            } else if ("ScrumVisible".equalsIgnoreCase(tagName)) {
                                status.setVisible((textValue.equals("true")));
                            } else if ("ScrumStatus".equalsIgnoreCase(tagName)) {
                                inStatus = false;
                            }
                        }
                        if (inEntry && !inStatus && inEffects && !inConditions) {
                            if ("ScrumEnum".equalsIgnoreCase(tagName)) {
                                effectOfAction.setEffect(EffectEnum.of(textValue));
                            } else if ("ScrumValue".equalsIgnoreCase(tagName)) {
                                effectOfAction.setValue(Integer.valueOf(textValue));
                            } else if ("ScrumStatus".equalsIgnoreCase(tagName)) {
                                effectOfAction.setStatus(status);
                            } else if ("ScrumSuccess".equalsIgnoreCase(tagName)) {
                                effectOfAction.setSuccess(textValue);
                            } else if ("ScrumFail".equalsIgnoreCase(tagName)) {
                                effectOfAction.setFail(textValue);
                            } else if ("ScrumConditions".equalsIgnoreCase(tagName)) {
                                effectOfAction.setConditionsOfEffect(conditionsOf);
                            } else if ("ScrumEffect".equalsIgnoreCase(tagName)) {
                                effectsOfAction.add(effectOfAction);
                            } else if ("ScrumEffects".equalsIgnoreCase(tagName)) {
                                inEffects = false;
                            }
                        }
                        if (inEntry && !inStatus && !inEffects && !inConditions) {
                            if ("ScrumHeroActions".equalsIgnoreCase(tagName)) {
                                inEntry = false;
                            } else if ("ScrumAction".equalsIgnoreCase(tagName)) {
                                scrumActions.add(scrumAction);
                            } else if ("ScrumIcon".equalsIgnoreCase(tagName)) {
                                int drawableResourceId = context.getResources().getIdentifier(textValue, "drawable", context.getPackageName());
                                scrumAction.setIconOfAction(drawableResourceId);
                            } else if ("ScrumName".equalsIgnoreCase(tagName)) {
                                scrumAction.setNameOfAction(textValue);
                            } else if ("ScrumDescription".equalsIgnoreCase(tagName)) {
                                scrumAction.setDescriptionOfAction(textValue);
                            } else if ("ScrumEffects".equalsIgnoreCase(tagName)) {
                                scrumAction.setEffectsOfAction(effectsOfAction);
                            } else if ("ScrumConditions".equalsIgnoreCase(tagName)) {
                                scrumAction.setConditionsOfAction(conditionsOf);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        }
        return isSuccess;
    }
}