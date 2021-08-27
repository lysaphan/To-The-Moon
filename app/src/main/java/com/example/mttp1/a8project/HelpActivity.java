package com.example.mttp1.a8project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HelpActivity extends AppCompatActivity {

    ImageButton ibHome;

    public void displayMainActivity(View view){
        Intent backHome = new Intent(this,MainActivity.class);
        startActivity(backHome);
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ibHome = findViewById(R.id.ibHome);
    }
}
