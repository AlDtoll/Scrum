package com.example.pusika.scrum.model.loader;

import android.content.Context;

import com.example.pusika.scrum.model.Fighter;
import com.example.pusika.scrum.model.Status;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

//todo сделать общий загрузчик
public class FighterResourseParser {
    private Fighter fighter;
    private Context context;

    public FighterResourseParser(Context context) {
        this.context = context;
    }

    public Fighter getFighter() {
        return fighter;
    }

    public boolean parse(XmlPullParser xpp) {
        boolean isSuccess = true;
        boolean inEntry = false;
        boolean inStatuses = false;
        Status status = null;
        String textValue = "";
        ArrayList<Status> statuses = new ArrayList<>();

        try {
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("ScrumEnemy".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            fighter = new Fighter();
                        } else if ("ScrumStatuses".equalsIgnoreCase(tagName)) {
                            inStatuses = true;
                        } else if ("ScrumStatus".equalsIgnoreCase(tagName)) {
                            status = new Status();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inStatuses) {
                            if ("ScrumName".equalsIgnoreCase(tagName)) {
                                status.setName(textValue);
                            } else if ("ScrumValue".equalsIgnoreCase(tagName)) {
                                status.setValue(Integer.valueOf(textValue));
                            } else if ("ScrumDescription".equalsIgnoreCase(tagName)) {
                                status.setDescription(textValue);
                            } else if ("ScrumVisible".equalsIgnoreCase(tagName)) {
                                status.setVisible((textValue.equals("true")));
                            } else if ("ScrumStatus".equalsIgnoreCase(tagName)) {
                                statuses.add(status);
                            } else if ("ScrumStatuses".equalsIgnoreCase(tagName)) {
                                inStatuses = false;
                            }
                        }
                        if (inEntry && !inStatuses) {
                            if ("ScrumEnemy".equalsIgnoreCase(tagName)) {
                                inEntry = false;
                            } else if ("ScrumName".equalsIgnoreCase(tagName)) {
                                fighter.setName(textValue);
                            } else if ("ScrumIcon".equalsIgnoreCase(tagName)) {
                                int drawableResourceId = context.getResources().getIdentifier(textValue, "drawable", context.getPackageName());
                                fighter.setIcon(drawableResourceId);
                            } else if ("ScrumDescription".equalsIgnoreCase(tagName)) {
                                fighter.setDescription(textValue);
                            } else if ("ScrummaxHp".equalsIgnoreCase(tagName)) {
                                fighter.setMaxHp(Integer.valueOf(textValue));
                            } else if ("ScrumHp".equalsIgnoreCase(tagName)) {
                                fighter.setHp(Integer.valueOf(textValue));
                            } else if ("ScrumDmg".equalsIgnoreCase(tagName)) {
                                fighter.setDmg(Integer.valueOf(textValue));
                            } else if ("ScrumStatuses".equalsIgnoreCase(tagName)) {
                                fighter.setStatuses(statuses);
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
