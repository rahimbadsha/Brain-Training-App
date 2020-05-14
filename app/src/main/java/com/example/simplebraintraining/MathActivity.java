package com.example.simplebraintraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MathActivity extends AppCompatActivity {

    public ArrayList<String> activityList = new ArrayList<>();
    MediaPlayer mediaPlayer;

    public void choosMath(View view)
    {
        activityList.clear();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            String level = bundle.getString("level");
            activityList.add(level);
        }

        String userMathSelectOptionValue = (String) view.getTag();
        activityList.add(userMathSelectOptionValue);

        mediaPlayer.start();
        Intent goGameStartActivity = new Intent(MathActivity.this, GameStart.class);
        goGameStartActivity.putExtra("secondLevel", activityList);
        startActivity(goGameStartActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mediaPlayer = MediaPlayer.create(MathActivity.this, R.raw.click4);
        if (mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if (item.getItemId() == android.R.id.home)
       {
           Intent intent = new Intent(MathActivity.this, LevelActivity.class);
           startActivity(intent);
           finish();
       }

       return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(MathActivity.this, LevelActivity.class);
        startActivity(intent);
        finish();
    }
}
