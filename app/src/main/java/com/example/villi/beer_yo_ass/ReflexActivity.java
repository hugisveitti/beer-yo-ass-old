package com.example.villi.beer_yo_ass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ReflexActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private RelativeLayout gameStats;
    private RelativeLayout resultsContainer;
    private TextView timerInfo;
    private TextView errorInfo;
    private TextView errors;
    private TextView greeting;
    private TextView input;
    private TextView results;
    private TextView scoreDisplay;
    private TextView drunkDescription;

    private Button restartButton;

    private final int height = 120;
    private final int width = 120;
    private TableLayout mTlayout;
    private TableRow tr;

    //private LinearLayout layout;
    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private int[][] multi = new int[alphabet.length][2];

    private int screenHeight;
    private int screenWidth;

    private TextView timerTextView;
    private long startTime = 0;

    private Boolean timeRunning = false;
    private Boolean gameRunning = false;
    private int errorCount = 0;
    private String alpha = "abcdefghijklmnopqrstuvwxyz";
    private String gameString = "";
    private int currentPlace = 1;
    private String[] drunkList = {"You are not even drunk, stop cheating!",
    "One beer, that´s all?! Step it up",
    "I have seen a preschooler drunker than you",
    "You could use a beer",
    "You can see clearly now the beer has gone",
    "You´re not as think as you drunk you are",
    "You are drinking like a big boy now",
    "Two beer or not two beers, you have had seven!",
    "Seeing double? You are making your mother proud!",
    "Your blood is literally 9% alcohol",
    "BEER YOUR ASS",
    "You are getting quite drunk sir, should I tell Jeeves to fetch a car?",
    "Please stop drinking, little Timmy needs money for surgery.",
    "Things are out of control, you are drunk like grandpa AGAIN!",
    "STOP in the name of lov... IPA",
    "I Would be surprised if you remember your name.",
    "I am an app and I am ashamed of you, get some help",
    "Call your ex and tell her you´ve changed, you know it´s a good idea.",
    "Thats it, Im cutting you off. GO HOME",
    "You are something beyond drunk, go home!"
    };
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            long mill = millis/10;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            mill = mill % 100;

            timerTextView.setText(String.format("%d:%02d:%02d", minutes, seconds,mill));

            timerHandler.postDelayed(this, 50);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex);

        gameStats = (RelativeLayout) findViewById(R.id.gameStats);
        resultsContainer = (RelativeLayout) findViewById(R.id.resultContainer);
        timerInfo = (TextView) findViewById(R.id.timerInfo);
        errorInfo = (TextView) findViewById(R.id.errorInfo);
        errors = (TextView) findViewById(R.id.errors);
        greeting = (TextView) findViewById(R.id.greeting);
        input = (TextView) findViewById(R.id.input);
        results = (TextView) findViewById(R.id.results);
        scoreDisplay = (TextView) findViewById(R.id.score);
        drunkDescription = (TextView) findViewById(R.id.drunkDescription);
        restartButton = (Button) findViewById(R.id.restartButton);
        gameStats.setVisibility(View.GONE);
        resultsContainer.setVisibility(View.GONE);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        mTlayout = (TableLayout) findViewById(R.id.mTlayout);
        createButtons(true);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });
    }

    private void restartGame() {
        gameStats.setVisibility(View.GONE);
        resultsContainer.setVisibility(View.GONE);
        timerTextView.setText("0");
        startTime = 0;
        timeRunning = false;
        gameRunning = false;
        currentPlace = 1;
        errorCount = 0;
        gameString = "";
        input.setText("Start typing");
        timerHandler.removeCallbacks(timerRunnable);
        createButtons(true);
    }

    private void createButtons(boolean first){
        mTlayout.removeAllViews();
        final String[] values;
        if(first){
            values = new String[alphabet.length];
            for (int i = 0; i < alphabet.length; i++){
                values[i] = String.valueOf(alphabet[i]);
            }
        }
        else{
            char[] alphabetTemp = alphabet.clone();
            values = shuffleArray(alphabetTemp);
        }
        for(int i = 0; i < alphabet.length; i++){
            if (i % 5 == 0) {
                tr = new TableRow(this);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                mTlayout.addView(tr);
            }
            final int id = i;
            Button btn = new Button(this);
            btn.setText(values[i]);
            btn.setId(i);
            btn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
            btn.setPadding(1,1,1,1);
            //btn.setBackgroundColor(getResources().getColor(R.color.buttonYellow));
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.width = 18;



            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!gameRunning) {
                        input.setText("...");
                        results.setText("");
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                        timeRunning = true;
                        gameRunning = true;
                        gameStats.setVisibility(View.VISIBLE);
                        resultsContainer.setVisibility(View.GONE);
                    }
                    if(gameRunning){
                        timerHandler.postDelayed(timerRunnable, 0);
                        if((gameString + values[id]).equals(alpha.substring(0,currentPlace))){
                            errors.setTextColor(getResources().getColor(R.color.black));
                            errors.setTextSize(14);
                            gameString = gameString + values[id];
                            input.setText(gameString.toUpperCase());
                            currentPlace++;
                            if(gameString.equals(alpha)){
                                gameOver();
                            }
                            else{
                                createButtons(false);
                            }
                        }
                        else{
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(500);
                            }
                            errorCount++;
                            errors.setText(String.valueOf(errorCount));
                            errors.setTextColor(getResources().getColor(R.color.errorRed));
                            errors.setTextSize(40);
                            createButtons(false);
                        }
                    }
                }
            });
            tr.addView(btn, lp);
        }

    }

    private void gameOver() {
        resultsContainer.setVisibility(View.VISIBLE);
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        int secondsConverted = seconds % 60;

        int score = seconds + errorCount*4;
        results.setText("Your score is");
        scoreDisplay.setText(String.valueOf(score));
        drunkDescription.setText(findDescription(score));
        sendToDatabase(score);

    }

    private void sendToDatabase(int score) {

    }

    private String findDescription(int score) {
        if(score < 60){
            return drunkList[0];
        }
        for(int i = 0; i < drunkList.length-1; i++){
            if(score < 60 + (i*10)){
                return drunkList[i];
            }
        }
        return drunkList[drunkList.length-1];
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        timeRunning = false;
    }


    private String[] shuffleArray(char[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        String[] ret = new String[ar.length];
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            char a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        for (int i = 0; i < alphabet.length; i++)
        {
            ret[i] = String.valueOf(ar[i]);
        }
        return ret;
    }


}
