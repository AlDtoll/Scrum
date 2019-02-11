package com.example.pusika.scrum.model.loader;

import android.content.Context;

import com.example.pusika.scrum.model.Hero;
import com.example.pusika.scrum.model.Status;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class HeroResourseParser {
    private Hero hero;
    private Context context;

    public HeroResourseParser(Context context) {
        this.context = context;
    }

    public Hero getHero() {
        return hero;
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
                        if ("ScrumHero".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            hero = new Hero();
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
                            if ("ScrumHero".equalsIgnoreCase(tagName)) {
                                inEntry = false;
                            } else if ("ScrumName".equalsIgnoreCase(tagName)) {
                                hero.setName(textValue);
                            } else if ("ScrumIcon".equalsIgnoreCase(tagName)) {
                                int drawableResourceId = context.getResources().getIdentifier(textValue, "drawable", context.getPackageName());
                                hero.setIcon(drawableResourceId);
                            } else if ("ScrumDescription".equalsIgnoreCase(tagName)) {
                                hero.setDescription(textValue);
                            } else if ("ScrummaxHp".equalsIgnoreCase(tagName)) {
                                hero.setMaxHp(Integer.valueOf(textValue));
                            } else if ("ScrumHp".equalsIgnoreCase(tagName)) {
                                hero.setHp(Integer.valueOf(textValue));
                            } else if ("ScrumDmg".equalsIgnoreCase(tagName)) {
                                hero.setDmg(Integer.valueOf(textValue));
                            } else if ("ScrumTimeAfterAttack".equalsIgnoreCase(tagName)) {
                                hero.setTimeAfterAttack(Integer.valueOf(textValue));
                            } else if ("ScrumTimeAfterDefence".equalsIgnoreCase(tagName)) {
                                hero.setTimeAfterDefence(Integer.valueOf(textValue));
                            } else if ("ScrumTimeAfterGetHit".equalsIgnoreCase(tagName)) {
                                hero.setTimeAfterGetHit(Integer.valueOf(textValue));
                            } else if ("ScrumThreshold".equalsIgnoreCase(tagName)) {
                                hero.setThreshold(Integer.valueOf(textValue));
                            } else if ("ScrumStatuses".equalsIgnoreCase(tagName)) {
                                hero.setStatuses(statuses);
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
