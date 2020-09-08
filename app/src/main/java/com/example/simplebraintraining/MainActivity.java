package com.example.simplebraintraining;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    TextView resultTextView;
    TextView sumTextView;
    TextView pointsTextView;
    TextView timerTextView;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button playAgainButton;

    LinearLayout highScoreLayout;
    LinearLayout gameLineyerLayout;
    TextView popupHighScoreText;
    TextView popupScoreText;
    MediaPlayer endGameMedia;
    MediaPlayer highScoreMedia;
    MediaPlayer buttonClickMedia;

    CountDownTimer timer;

    private long backPressedTime;

    Dialog dialog;

    List<Integer> answer = new ArrayList<>();
    ArrayList<String> levelSelection = new ArrayList<>();

    int locationOfCorrectAnswer = 0;
    int score = 0;
    int incorrectAnswer = 0;
    int numberOfQuestions = 0;
    int loadScores = 0;
    Boolean isActive = true;
    int generateRandomValue = 1;
    String generateRandomMathTerm = "+";

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    public int loadScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("gameScore", Context.MODE_PRIVATE);
        int scores = sharedPreferences.getInt("lastScore", 0);
        return scores;
    }

    public void saveScore(int score) {
        SharedPreferences sharedPreferences = getSharedPreferences("gameScore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastScore", score);
        editor.commit();
    }

    public void showDialog(final Activity activity, String msg) {

        dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.gamepopuplayout);

        popupScoreText = dialog.findViewById(R.id.popupScoreTextViewId);
        TextView popupCloseButton = dialog.findViewById(R.id.closeTextViewId);
        TextView popupTextHighScore = dialog.findViewById(R.id.popupTextHighScoreId);
        final TextView popupYesButton = dialog.findViewById(R.id.popupYesTextId);
        TextView popupNoButton = dialog.findViewById(R.id.popupNoTextId);
        popupHighScoreText = dialog.findViewById(R.id.popupScoreTextViewId);
        highScoreLayout = dialog.findViewById(R.id.highScoreLayoutId);
        gameLineyerLayout = dialog.findViewById(R.id.gameLineyerLayout);

        gameLineyerLayout.animate().scaleXBy(0.1f).setDuration(1000);

        loadScores = loadScore();
        popupTextHighScore.setText("Last Score: " + valueOf(loadScores));

        if (loadScores == 0) {
            if (score == 0) {
                endGameMedia.start();
                popupScoreText.setText(msg);
            } else {
                highScoreMedia.start();

                saveScore(score);

                highScoreLayout.setVisibility(View.VISIBLE);
                popupHighScoreText.setTextColor(Color.GREEN);
                popupScoreText.setText(msg);
                popupTextHighScore.setText("Last Score: " + valueOf(loadScores));
            }
        } else {
            if (score > loadScores) {
                highScoreMedia.start();

                saveScore(score);

                highScoreLayout.setVisibility(View.VISIBLE);
                popupHighScoreText.setTextColor(Color.GREEN);
                popupScoreText.setText(msg);
                popupTextHighScore.setText("Last Score: " + valueOf(loadScores));
            } else {
                endGameMedia.start();
                popupScoreText.setText(msg);
            }
        }

        popupYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                highScoreMedia.stop();
                endGameMedia.stop();
                playAgain(popupYesButton);
                onStop();
            }
        });

        popupCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highScoreMedia.stop();
                endGameMedia.stop();
                onStop();
            }
        });

        popupNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    highScoreMedia.stop();
                    endGameMedia.stop();
                    onStop();
                    Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        highScoreMedia.stop();
                        endGameMedia.stop();
                        onStop();
                        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void playAgain(View view) {
        score = 0;
        numberOfQuestions = 0;
        isActive = true;

        timerTextView.setText("30s");
        pointsTextView.setText("0/0");
        resultTextView.setText("");

        generateQuestion();

       timer = new CountDownTimer(40500, 2000) {
            @Override
            public void onTick(long millisUnitlFinished) {

                timerTextView.setText(valueOf(millisUnitlFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {

                isActive = false;
                resultTextView.setVisibility(View.INVISIBLE);
                resultTextView.setText("Total Question: " + Integer.toString(numberOfQuestions) +
                        "\nYour Score: " + Integer.toString(score));
                final String value = resultTextView.getText().toString();

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    showDialog(MainActivity.this, value);
                }

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        showDialog(MainActivity.this, value);
                    }
                });
            }
        }.start();
    }


    public void generateQuestion() {
        Random rand = new Random();

        int a = rand.nextInt(generateRandomValue);
        int b = rand.nextInt(generateRandomValue);

        while (b == 0) {
            b = rand.nextInt(generateRandomValue);
        }


        sumTextView.setText(
                Integer.toString(a) + "  " + levelSelection.get(1) +
                        " " + Integer.toString(b) + " = ?");

        locationOfCorrectAnswer = rand.nextInt(4);

        answer.clear();

        int incorrectAnswer = 0;

        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrectAnswer) {
                switch (levelSelection.get(1)) {
                    case "+":
                        answer.add(a + b);
                        break;
                    case "-":
                        answer.add(a - b);
                        break;
                    case "*":
                        answer.add(a * b);
                        break;
                    case "/":
                        answer.add(a / b);
                        break;
                }
            } else {
                switch (levelSelection.get(1)) {
                    case "+":
                        incorrectAnswer = rand.nextInt(generateRandomValue + generateRandomValue);
                        if ((answer.contains(incorrectAnswer))) {
                            while (incorrectAnswer == a + b) {
                                incorrectAnswer = rand.nextInt(generateRandomValue + generateRandomValue);
                            }
                        }
                        answer.add(incorrectAnswer);
                        break;
                    case "-":
                        incorrectAnswer = rand.nextInt(generateRandomValue);
                        while (answer.contains(incorrectAnswer)) {
                            incorrectAnswer = rand.nextInt(generateRandomValue * generateRandomValue);
                        }
                        while (incorrectAnswer == a - b) {
                            incorrectAnswer = rand.nextInt(generateRandomValue);
                        }
                        answer.add(incorrectAnswer);
                        break;
                    case "*":
                        incorrectAnswer = rand.nextInt(generateRandomValue * generateRandomValue);
                        while (answer.contains(incorrectAnswer)) {
                            incorrectAnswer = rand.nextInt(generateRandomValue * generateRandomValue);
                        }
                        while (incorrectAnswer == a * b) {
                            incorrectAnswer = rand.nextInt(generateRandomValue * generateRandomValue);
                        }
                        answer.add(incorrectAnswer);
                        break;
                    case "/":
                        incorrectAnswer = rand.nextInt(10);
                        while (answer.contains(incorrectAnswer)) {
                            incorrectAnswer = rand.nextInt(10);
                        }
                        while (incorrectAnswer == a / b) {
                            incorrectAnswer = rand.nextInt(10);
                        }
                        answer.add(incorrectAnswer);
                        break;
                }
            }
        }

        button0.setText(Integer.toString(answer.get(0)));
        button1.setText(Integer.toString(answer.get(1)));
        button2.setText(Integer.toString(answer.get(2)));
        button3.setText(Integer.toString(answer.get(3)));
    }

    public void chooseAnswer(View view) {
        if (isActive) {
            buttonClickMedia = MediaPlayer.create(getApplication(), R.raw.click4);
            buttonClickMedia.start();
            if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
                score++;
                resultTextView.setVisibility(View.VISIBLE);
                resultTextView.setText("Correct!");
                resultTextView.setTextColor(Color.GREEN);
            } else {
                resultTextView.setVisibility(View.VISIBLE);
                resultTextView.setText("Wrong!");
                resultTextView.setTextColor(Color.RED);
            }

            numberOfQuestions++;
            pointsTextView.setText(Integer.toString(score) + "/" + Integer.toString(numberOfQuestions));
            generateQuestion();
        }
    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis())
        {
            finishGame();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Press back again to finish the game", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void finishGame() {
        timer.cancel();
        isActive = false;
        resultTextView.setVisibility(View.INVISIBLE);
        resultTextView.setText("Total Question: " + Integer.toString(numberOfQuestions) +
                "\nYour Score: " + Integer.toString(score));
        final String value = resultTextView.getText().toString();


        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            showDialog(MainActivity.this, value);
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                showDialog(MainActivity.this, value);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null)
        {
            timer.cancel();
        }

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        buttonClickMedia.stop();
        highScoreMedia.stop();
        endGameMedia.stop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        sumTextView = findViewById(R.id.sumTextViewId);
        pointsTextView = findViewById(R.id.pointsTextViewId);
        resultTextView = findViewById(R.id.resultTextViewId);
        timerTextView = findViewById(R.id.timerTextViewId);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        playAgainButton = findViewById(R.id.playAgainButtonId);
        highScoreMedia = MediaPlayer.create(getApplication(), R.raw.highscore);
        endGameMedia = MediaPlayer.create(getApplication(), R.raw.gameend);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.mainActivityadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            levelSelection.clear();
            levelSelection = bundle.getStringArrayList("lastString");
            Toast.makeText(this, "Start Playing", Toast.LENGTH_SHORT).show();

            switch (levelSelection.get(0)) {
                case "0":
                    generateRandomValue = 11;
                    playAgain(playAgainButton);
                    break;

                case "1":
                    generateRandomValue = 21;
                    playAgain(playAgainButton);
                    break;
                case "2":
                    generateRandomValue = 51;
                    playAgain(playAgainButton);
                    break;
            }
        }
    }
}
