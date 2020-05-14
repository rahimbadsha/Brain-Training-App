package com.example.simplebraintraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LevelActivity extends AppCompatActivity  {

    MediaPlayer mediaPlayer;

    public void  chooseLevel(View view)
    {
        String gameLavel = (String) view.getTag();

        mediaPlayer.start();
        Intent goMathActivity = new Intent(LevelActivity.this, MathActivity.class);
        goMathActivity.putExtra("level", gameLavel);
        startActivity(goMathActivity);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mediaPlayer = MediaPlayer.create(LevelActivity.this, R.raw.click4);
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
