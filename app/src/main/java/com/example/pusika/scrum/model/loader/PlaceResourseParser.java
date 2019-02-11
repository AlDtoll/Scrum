package com.example.pusika.scrum.model.loader;

import android.content.Context;

import com.example.pusika.scrum.model.Place;
import com.example.pusika.scrum.model.Status;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class PlaceResourseParser {
    private Place place;
    private Context context;

    public PlaceResourseParser(Context context) {
        this.context = context;
    }

    public Place getPlace() {
        return place;
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
                        if ("ScrumPlace".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            place = new Place();
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
                            if ("ScrumPlace".equalsIgnoreCase(tagName)) {
                                inEntry = false;
                            } else if ("ScrumName".equalsIgnoreCase(tagName)) {
                                place.setName(textValue);
                            } else if ("ScrumIcon".equalsIgnoreCase(tagName)) {
                                int drawableResourceId = context.getResources().getIdentifier(textValue, "drawable", context.getPackageName());
                                place.setIcon(drawableResourceId);
                            } else if ("ScrumDescription".equalsIgnoreCase(tagName)) {
                                place.setDescription(textValue);
                            } else if ("ScrumStatuses".equalsIgnoreCase(tagName)) {
                                place.setStatuses(statuses);
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
