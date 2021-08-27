package com.example.mttp1.a8project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GameOverActivity extends AppCompatActivity {
    EditText et;
    ImageButton saveButton;
    ImageButton ibHS, ibHome, ibReplay;
    String name;
    TextView tvEnd, tvScore, tvMessage;
    boolean firstClick = true;

    private SoundPlayer sound;

    public void displayMainActivity(View view){
        Intent backHome = new Intent(this,MainActivity.class);
        startActivity(backHome);
        finishAffinity();
    }

    public void displayPlayActivity(View view){
        Intent startGame = new Intent(this,PlayActivity.class);
        startActivity(startGame);
        finishAffinity();
    }

    public void displayScoreActivity(View view){
        Intent viewTopScore = new Intent(this,ScoreActivity.class);
        startActivity(viewTopScore);
        finishAffinity();
    }

    public String getScore() {return String.valueOf(PlayActivity.finalScore);}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        saveButton = findViewById(R.id.saveButton);

        et = findViewById(R.id.editText);
        ibHS = findViewById(R.id.ibHS);
        ibHome = findViewById(R.id.ibHome);
        ibReplay = findViewById(R.id.ibReplay);

        tvEnd = findViewById(R.id.tvEnd);
        tvScore = findViewById(R.id.tvScore);
        tvMessage = findViewById(R.id.tvMessage);

        sound = new SoundPlayer(this);


        //Display the score
        tvScore.setText(String.valueOf(PlayActivity.finalScore));

        //Check if it is in top 5 highest score
        ScoreActivity.checkHighScore(PlayActivity.finalScore);

        if (ScoreActivity.update == true)
        {
            tvEnd.setText("High Score");
            tvMessage.setVisibility(View.INVISIBLE);
            et.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);
        }

        Log.d("242", "The score is " + getScore());
        Log.d("242", "The name is: " + et.getText().toString());

        saveButton.setOnClickListener(new ImageButton.OnClickListener(){
                public void onClick(View v){
                    if (firstClick)
                    {
                        if (et.getText().toString().equals(""))
                            reminder();
                        else
                        {
                            name = et.getText().toString();
                            Log.d("242", "Player's name: " + name);
                            saveName();
                            ScoreActivity.updateScore(PlayActivity.finalScore, name);
                            firstClick = false;
                        }
                    }
                    else
                        saveName();
                }
        });

    }

    private void saveName()
    {
        if (firstClick)
            Toast.makeText(this, "You have saved your score.", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "You have already saved your score.", Toast.LENGTH_LONG).show();
    }

    private void reminder()
    {
        Toast.makeText(this, "Please enter your name.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (MainActivity.soundOn == true)
            sound.playLoseSound();

    }
}
