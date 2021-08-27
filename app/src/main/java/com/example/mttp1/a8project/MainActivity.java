package com.example.mttp1.a8project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static boolean soundOn = true;
    public static boolean musicOn = true;
    ImageButton ibPlay, ibScore, ibQuit, ibSound, ibMusic;
    static Intent bss;

    public void displayPlayActivity(View view){
        Intent startGame = new Intent(this,PlayActivity.class);
        startActivity(startGame);
    }

    public void displayScoreActivity(View view){
        Intent scoreChart = new Intent(this,ScoreActivity.class);
        startActivity(scoreChart);
    }

    public void displayHelpActivity(View view){
        Intent instruction = new Intent(this,HelpActivity.class);
        startActivity(instruction);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ibMusic = findViewById(R.id.ibMusic);
        ibSound = findViewById(R.id.ibSound);
        ibPlay = findViewById(R.id.play_button);
        ibScore = findViewById(R.id.score_button);
        ibQuit = findViewById(R.id.quit_button);

        if (soundOn == true)
            ibSound.setImageResource(R.drawable.soundon);
        else
            ibSound.setImageResource(R.drawable.soundoff);

        bss = new Intent(this, BackgroundSoundService.class);

        if (musicOn == true)
        {
            ibMusic.setImageResource(R.drawable.musicon);
            startService(bss);
        }
        else
            ibMusic.setImageResource(R.drawable.musicoff);


        //When the user clicks this button, it will exit the app
        ibQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                stopService(bss);
                finish();
                System.exit(0);
            }
        });


        ibSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //When the user clicks this button, the sound will turn off
                if (soundOn == true)
                {
                    soundOn = false;
                    ibSound.setImageResource(R.drawable.soundoff);
                    sound();
                    Log.d("242", "The sound is on: " + soundOn);
                }

                else //When the user clicks this button, the sound will turn on
                {
                    soundOn = true;
                    ibSound.setImageResource(R.drawable.soundon);
                    sound();
                    Log.d("242", "The sound is on: " + soundOn);
                }
            }
        });

        ibMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //When the user clicks this button, the sound will turn off
                if (musicOn == true)
                {
                    musicOn = false;
                    ibMusic.setImageResource(R.drawable.musicoff);
                    stopService(bss);
                    music();
                }
                else //When the user clicks this button, the sound will turn on
                {
                    musicOn = true;
                    ibMusic.setImageResource(R.drawable.musicon);
                    startService(bss);
                    music();
                }
                Log.d("242", "The music is on: " + musicOn);
            }
        });
    }

    private void sound()
    {
        if (soundOn == true)
        Toast.makeText(this, "The sound is on.", Toast.LENGTH_SHORT).show();

        if (soundOn == false)
            Toast.makeText(this, "The sound is off.", Toast.LENGTH_SHORT).show();
    }

    private void music()
    {
        if (musicOn == true)
            Toast.makeText(this, "The music is on.", Toast.LENGTH_SHORT).show();

        if (musicOn == false)
            Toast.makeText(this, "The music is off.", Toast.LENGTH_SHORT).show();
    }
}
