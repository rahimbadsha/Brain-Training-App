package com.example.simplebraintraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash_screen);


        progressBar = findViewById(R.id.progressBarId);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                flashScreenTime();
                goToLevelActivity();
            }
        });

        thread.start();
    }

    public void flashScreenTime()
    {
        for (int progress = 30; progress <= 90; progress = progress + 30)
        {
            try {

                Thread.sleep(800);
                progressBar.setProgress(progress);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }

    public void goToLevelActivity()
    {
        Intent intent = new Intent(getApplicationContext(), LevelActivity.class);
        startActivity(intent);
        finish();
    }
}
