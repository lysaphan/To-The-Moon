package com.example.mttp1.a8project;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundSoundService extends Service {

    public static MediaPlayer player;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.moonlight);
        player.setLooping(true); // Set looping
        player.setVolume(300,300);
    }

    public void onStart(Intent intent, int startId) {
        player.start();
    }

    @Override
    public void onDestroy() {
        player.stop();
    }
}