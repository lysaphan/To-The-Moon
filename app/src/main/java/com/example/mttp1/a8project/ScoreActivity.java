package com.example.mttp1.a8project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    ImageButton ibHome;
    TextView s1, s2, s3, s4, s5, tvP1, tvP2, tvP3, tvP4, tvP5;

    static int[] scoreList = new int[]{5,4,3,2,1};
    static String[] playerList = new String[]{"Player 1","Player 2","Player 3", "Player 4", "Player 5"};
    static boolean update = false;
    static int position;
    public void displayMainActivity(View view){
        Intent backHome = new Intent(this,MainActivity.class);
        startActivity(backHome);
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ibHome = findViewById(R.id.ibHome);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        s4 = findViewById(R.id.s4);
        s5 = findViewById(R.id.s5);

        tvP1 = findViewById(R.id.p1);
        tvP2 = findViewById(R.id.p2);
        tvP3 = findViewById(R.id.p3);
        tvP4 = findViewById(R.id.p4);
        tvP5 = findViewById(R.id.p5);


        Log.d("242", "S0 = " + scoreList[0]);
        Log.d("242", "P3 = " + playerList[3]);

        if (playerList[0] != null)
        {
            tvP1.setText(playerList[0]);
            s1.setText(String.valueOf(scoreList[0]));
        }
        if (playerList[1] != null)
        {
            tvP2.setText(playerList[1]);
            s2.setText(String.valueOf(scoreList[1]));
        }
        if (playerList[2] != null)
        {
            tvP3.setText(playerList[2]);
            s3.setText(String.valueOf(scoreList[2]));
        }
        if (playerList[3] != null)
        {
            tvP4.setText(playerList[3]);
            s4.setText(String.valueOf(scoreList[3]));
        }
        if (playerList[4] != null)
        {
            tvP5.setText(playerList[4]);
            s5.setText(String.valueOf(scoreList[4]));
        }

    }

    public static void checkHighScore(int score)
    {
        for (int i = 0; i < scoreList.length; i++)
        {
            if (score > scoreList[i])
            {
                position = i;
                update = true;
                Log.d("242", "Position: " + position);
                break;
            }
        }
    }

    public static void updateScore(int score, String playerName)
    {
        for (int i = scoreList.length-1; i >= position; i--)
        {
            scoreList[i] = scoreList[i-1];
            playerList[i] = playerList[i-1];
        }
        scoreList[position] = score;
        playerList[position] = playerName;
    }

}
