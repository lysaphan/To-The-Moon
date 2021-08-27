package com.example.mttp1.a8project;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int moonSound;
    private static int explodeSound;
    private static int loseSound;


    public SoundPlayer(Context context){
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        moonSound = soundPool.load(context,R.raw.moonsound,1);
        explodeSound = soundPool.load(context,R.raw.explodesound,1);
        loseSound = soundPool.load(context,R.raw.lose,1);
    }

    public void playMoonSound(){
        soundPool.play(moonSound, 1.0f,1.0f,1,0,1.0f);
    }

    public void playExplodeSound(){
        soundPool.play(explodeSound, 0.50f,0.5f,1,0,1.0f);
    }

    public void playLoseSound(){
        soundPool.play(loseSound, 1.0f,1.0f,1,0,1.0f);
    }
}