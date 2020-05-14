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

import java.util.ArrayList;

public class GameStart extends AppCompatActivity {

    public ArrayList<String> activityList2 = new ArrayList<>();
    MediaPlayer mediaPlayer;

    public void playGame(View view)
    {
        final TextView buttonText = findViewById(R.id.gameStartButtonId);
        final TextView timerTextView = findViewById(R.id.gameStartTimerText);
        buttonText.setVisibility(View.GONE);
        timerTextView.setVisibility(View.VISIBLE);
        timerTextView.setText("0s");
        timerTextView.animate().rotation(360f).setDuration(3000);

        new CountDownTimer(3050, 1000)
        {
            @Override
            public void onTick(long millisUnitFinished) {

                mediaPlayer.start();
                timerTextView.setText(String.valueOf(millisUnitFinished/1000) + "s");
            }

            @Override
            public void onFinish() {

                activityList2.clear();

                Bundle bundle = getIntent().getExtras();
                if (bundle != null)
                {
                    ArrayList<String> value = bundle.getStringArrayList("secondLevel");

                    String level = value.get(0);
                    String mathSelection = value.get(1);

                    activityList2.add(level);
                    activityList2.add(mathSelection);
                }

                mediaPlayer.stop();
                Intent intent = new Intent(GameStart.this, MainActivity.class);
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

        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.countdown);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(GameStart.this, LevelActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
