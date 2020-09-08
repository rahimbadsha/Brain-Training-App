package com.example.simplebraintraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class GameStart extends AppCompatActivity {

    public ArrayList<String> activityList2 = new ArrayList<>();
    MediaPlayer mediaPlayer;

    private AdView mAdView;

    public void playGame(View view) {
        final TextView buttonText = findViewById(R.id.gameStartButtonId);
        final TextView timerTextView = findViewById(R.id.gameStartTimerText);
        buttonText.setVisibility(View.GONE);
        timerTextView.setVisibility(View.VISIBLE);
        timerTextView.setText("0s");
        timerTextView.animate().rotation(360f).setDuration(3000);

        new CountDownTimer(3050, 1000) {
            @Override
            public void onTick(long millisUnitFinished) {

                mediaPlayer.start();
                timerTextView.setText(String.valueOf(millisUnitFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {

                activityList2.clear();

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    ArrayList<String> value = bundle.getStringArrayList("secondLevel");

                    String level = value.get(0);
                    String mathSelection = value.get(1);

                    activityList2.add(level);
                    activityList2.add(mathSelection);
                }

                mediaPlayer.stop();
                Intent intent = new Intent(GameStart.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("lastString", activityList2);
                startActivity(intent);
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game_start);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewGameStart);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.countdown);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(GameStart.this, LevelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
