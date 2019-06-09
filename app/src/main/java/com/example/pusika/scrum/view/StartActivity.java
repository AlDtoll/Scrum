package com.example.pusika.scrum.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pusika.scrum.R;
import com.example.pusika.scrum.model.Fighter;
import com.example.pusika.scrum.model.Hero;
import com.example.pusika.scrum.model.Place;
import com.example.pusika.scrum.model.ScrumAction;
import com.example.pusika.scrum.model.loader.FighterResourseParser;
import com.example.pusika.scrum.model.loader.HeroResourseParser;
import com.example.pusika.scrum.model.loader.PlaceResourseParser;
import com.example.pusika.scrum.model.loader.ScrumActionResourseParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class StartActivity extends AppCompatActivity {

    public final static String HERO = "hero";
    public final static String ENEMY = "enemy";
    public final static String PLACE = "place";
    public final static String HERO_ACTIONS = "hero_actions";
    public final static String AUTO_ACTIONS = "auto_actions";

    /**
     * Блок с вьюхами
     */
    TextView titleTextView;

    TextView nameOfPlace;
    ImageView iconOfPlace;
    TextView descriptionOfPlace;

    TextView nameOfHero;
    ImageView iconOfHero;
    TextView descriptionOfHero;

    TextView nameOfEnemy;
    ImageView iconOfEnemy;
    TextView descriptionOfEnemy;

    TextView infoTextView;
    Button startButton;

    /**
     * Блок с данными от загрузчиков
     */
    private Place place = new Place();
    private Hero hero = new Hero();
    private Fighter enemy = new Fighter();
    private String title = "Эпичный заголовок";
    private ArrayList<ScrumAction> scrumHeroActions = new ArrayList<>();
    private ArrayList<ScrumAction> scrumAutoActions = new ArrayList<>();

    private static boolean hasConnection(final Context context) {
        //todo Проверка разрешений
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = connectivityManager.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findComponentsById();
        if (hasConnection(this)) {
            infoTextView.setText("Макаем перо...");
            Loader loader = new Loader();
            loader.execute(getAddress());
        } else {
            infoTextView.setText("Что-то вдохновения нет. И интернета нет");
        }
    }

    private void findComponentsById() {
        titleTextView = findViewById(R.id.title);

        nameOfPlace = findViewById(R.id.nameOfPlace);
        iconOfPlace = findViewById(R.id.iconOfPlace);
        descriptionOfPlace = findViewById(R.id.descriptionOfPlace);

        nameOfHero = findViewById(R.id.nameOfHero);
        iconOfHero = findViewById(R.id.iconOfHero);
        descriptionOfHero = findViewById(R.id.descriptionOfHero);

        nameOfEnemy = findViewById(R.id.nameOfEnemy);
        iconOfEnemy = findViewById(R.id.iconOfEnemy);
        descriptionOfEnemy = findViewById(R.id.descriptionOfEnemy);

        infoTextView = findViewById(R.id.infoTextView);
        startButton = findViewById(R.id.startButton);
    }

    private String getAddress() {
        //todo выбор странички в зависимости от человека
        //todo сохранение в преференсы кода битвы, чтобы два раза в одно приключение не гонять
        return "http://scrum.ucoz.site/index/demo/0-6/";
    }

    public void startAdventure(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(PLACE, place);
        intent.putExtra(HERO, hero);
        intent.putExtra(ENEMY, enemy);
//        //todo перкидывать сервисом или бродкастом в диалог
        intent.putExtra(HERO_ACTIONS, scrumHeroActions);
        intent.putExtra(AUTO_ACTIONS, scrumAutoActions);
        startActivity(intent);
    }

    private boolean processContent(String content) {
        // todo переделать на фабрику
        XmlPullParser xmlPullParser = Xml.newPullParser();
        int processSuccessValue = 0;

        //todo сделать нормально
        title = content.substring(content.indexOf("<ScrumTitle"), content.indexOf("</ScrumTitle>"));
        title = title.substring(title.indexOf("<ScrumText"), title.indexOf("</ScrumText>"));
        title = title.substring(11);

        //todo для загрузчиков сделать проверки надйенных элементов
        String contentForPlace = content.substring(content.indexOf("<ScrumPlace"), content.indexOf("</ScrumPlace>"));
        try {
            xmlPullParser.setInput(new StringReader(contentForPlace));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        try {
            PlaceResourseParser placeResourseParser = new PlaceResourseParser(this);
            if (placeResourseParser.parse(xmlPullParser)) {
                place = placeResourseParser.getPlace();
                processSuccessValue++;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        String contentForHero = content.substring(content.indexOf("<ScrumHero"), content.indexOf("</ScrumHero>"));
        try {
            xmlPullParser.setInput(new StringReader(contentForHero));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            HeroResourseParser heroResourseParser = new HeroResourseParser(this);
            if (heroResourseParser.parse(xmlPullParser)) {
                hero = heroResourseParser.getHero();
                processSuccessValue++;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        String contentForEnemy = content.substring(content.indexOf("<ScrumEnemy"), content.indexOf("</ScrumEnemy>"));
        try {
            xmlPullParser.setInput(new StringReader(contentForEnemy));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            FighterResourseParser enemyResourseParser = new FighterResourseParser(this);
            if (enemyResourseParser.parse(xmlPullParser)) {
                enemy = enemyResourseParser.getFighter();
                processSuccessValue++;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //todo вынести в отдельную загрузку
        String contentForAction = content.substring(content.indexOf("<ScrumHeroActions"), content.indexOf("</ScrumHeroActions>"));
        try {
            xmlPullParser.setInput(new StringReader(contentForAction));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            ScrumActionResourseParser scrumActionResourseParser = new ScrumActionResourseParser(this);
            if (scrumActionResourseParser.parse(xmlPullParser)) {
                scrumHeroActions = scrumActionResourseParser.getActions();
                processSuccessValue++;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        String contentForAutoAction = content.substring(content.indexOf("<ScrumAutoActions"), content.indexOf("</ScrumAutoActions>"));
        try {
            xmlPullParser.setInput(new StringReader(contentForAutoAction));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            ScrumActionResourseParser scrumActionResourseParser = new ScrumActionResourseParser(this);
            if (scrumActionResourseParser.parse(xmlPullParser)) {
                scrumAutoActions = scrumActionResourseParser.getActions();
                processSuccessValue++;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return processSuccessValue == 5;
    }

    private void setPresentation() {
        //todo сделать подгрузку картинок
        String placeOfScrum = "Место схватки: " + place.getName();
        nameOfPlace.setText(placeOfScrum);
        iconOfPlace.setImageResource(place.getIcon());
        descriptionOfPlace.setText(place.getDescription());

        nameOfHero.setText(hero.getName());
        iconOfHero.setImageResource(hero.getIcon());
        descriptionOfHero.setText(hero.getDescription());

        nameOfEnemy.setText(enemy.getName());
        iconOfEnemy.setImageResource(enemy.getIcon());
        descriptionOfEnemy.setText(enemy.getDescription());

        titleTextView.setText(title);
    }

    public class Loader extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {
            String content;
            try {
                content = getContent(path[0]);
            } catch (IOException ex) {
                content = ex.getMessage();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String content) {
            if (processContent(content)) {
                infoTextView.setText("Приключение готово");
                startButton.setEnabled(true);
                setPresentation();
            } else {
                infoTextView.setText("Сильно макнули перо, получились одни кляксы. Исправим попозже");
            }
        }

        private String getContent(String path) throws IOException {
            BufferedReader reader = null;
            try {
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();
                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                return buf.toString();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }

}
