package com.example.mttp1.a8project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)

public class PlayActivity extends AppCompatActivity implements SensorEventListener {

    private SoundPlayer sound;

    Random rand = new Random();

    //Constant variables
    private static final int BALL_SIZE = 200;
    private static final int ASTEROID_SIZE = 250;
    private static final int SMALL_ASTEROID_SIZE = 70;
    private static final int MOON_SIZE = 400;
    private static final int GIRL_SIZE = 150;

    public static final int SWIPE_THRESHOLD = 100;
    public static final int SWIPE_VELOCITY_THRESHOLD = 200;

    static final String STATE_LIFE = "playerLife";
    static final String STATE_SCORE = "playerScore";
    static final String STATE_LEVEL = "playerLevel";
    public static int finalScore;

    //Declare variables
    public boolean onFlingOccur = false, result = false, mCollision = false;

    public int score = 0, level = 1, heartCount = 3;

    private float bX = getScreenWidth()/2 - BALL_SIZE/2;
    private float bY = getScreenHeight();
    private float xV = 0.0f;
    private float yV = 0.0f;
    private float xMax, yMax;

    SensorManager sm;
    GestureDetectorCompat gestureDetector;
    MyGestureListener gestureListener;

    class MyGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("COMPX242", "Down");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("COMPX242", "Show press");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("COMPX242", "Single tap up");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("COMPX242", "Scroll " + distanceX + ", " + distanceY);
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("COMPX202", "Long press");
        }

        ///////////////////////////OnFling//////////////////////////////////
        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {

            //if (bY + BALL_SIZE == getScreenHeight())
            //{
                xV = -velocityX / 100;
                yV = -velocityY / 100;
                result = false; //did not do any kind of behaviour based on this event
                float diffY = moveEvent.getY() - downEvent.getY(); //how far did the user's finger traverse along the Y axis
                float diffX = moveEvent.getX() - downEvent.getX(); //movement across X axis

                //which was greater? movement across Y or X?
                if (Math.abs(diffX) > Math.abs(diffY)) //swipe right of left
                {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                        result = true; //don't pass this event to any other method
                }
                else //swipe up or down
                {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
                        result = true;
                }
                onFlingOccur = true;
            //}
            return result;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(STATE_SCORE, score);
        savedInstanceState.putInt(STATE_LEVEL, level);
        savedInstanceState.putInt(STATE_LIFE, heartCount);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public ImageButton createImageButton(int rID, int x, int y)
    {
        ImageButton ib = new ImageButton(this);
        ib.setImageResource(rID);
        ib.setBackground(null);
        ib.setX(x);
        ib.setY(y);
        return ib;
    }

    ///////////////////////////OnCreate//////////////////////////////////
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("242", "OnCreate");

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            score = savedInstanceState.getInt(STATE_SCORE);
            level = savedInstanceState.getInt(STATE_LEVEL);
            heartCount = savedInstanceState.getInt(STATE_LIFE);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);

        //Remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        RelativeLayout layout = new RelativeLayout(this);

        BitmapView bmView = new BitmapView(this);
        layout.addView(bmView);

        StatusBarView statusBarView = new StatusBarView(this);
        layout.addView(statusBarView);

        MoonView moonView = new MoonView(this);
        layout.addView(moonView);

        BallView ballView = new BallView(this);
        layout.addView(ballView);

        for (int i = 0; i < level-1; i++)
        {
            int asteroidX = rand.nextInt(getScreenWidth()-SMALL_ASTEROID_SIZE);
            int asteroidY = rand.nextInt(1800-SMALL_ASTEROID_SIZE - 1050) + 1050;

            AsteroidView asteroidView = new AsteroidView(this, asteroidX, asteroidY);
            layout.addView(asteroidView);
        }

        MovingAsteroidView mAsteroidView1 = new MovingAsteroidView(this, 0, 800);
        layout.addView(mAsteroidView1);
        MovingAsteroidView mAsteroidView2 = new MovingAsteroidView(this, getScreenWidth() - ASTEROID_SIZE, 1800);
        layout.addView(mAsteroidView2);


        ImageButton ibReplay = createImageButton(R.drawable.replaybutton,1010,-35);
        layout.addView(ibReplay);

        ImageButton ibBack = createImageButton(R.drawable.backbutton,1140,-35);
        layout.addView(ibBack);

        ImageButton ibExit = createImageButton(R.drawable.exitbutton,1270,-35);
        layout.addView(ibExit);

        setContentView(layout);

        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        xMax = (float) size.x - 200;
        yMax = (float) size.y - 200;

        sound = new SoundPlayer(this);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        gestureListener = new MyGestureListener();
        gestureDetector = new GestureDetectorCompat(this, gestureListener);

        //Move to game over activity when the game ends
        Intent gameover = new Intent(this,GameOverActivity.class);
        if (heartCount == 0) {
            finalScore = score;
            startActivity(gameover);
        }

        final GestureDetector gestureDetector = new GestureDetector(new MyGestureListener());
        ballView.setOnTouchListener(new BallView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        final Intent newGame = new Intent(this,PlayActivity.class);
        ibReplay.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                startActivity(newGame);
                startNewGame();
            }
        });

        final Intent backHome = new Intent(this,MainActivity.class);
        ibBack.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                startActivity(backHome);
                finishAffinity();
            }
        });

        ibExit.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                stopService(MainActivity.bss);
                finishAffinity();
                finish();
                System.exit(0);
            }
        });

    }

    ///////////////////////////OnSensorChanged//////////////////////////////////
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float xA = sensorEvent.values[0];
            float yA = -sensorEvent.values[1];
            updateBall(xA, yA);

        }
    }

    ///////////////////////////updateBall//////////////////////////////////
    private void updateBall(float xA, float yA) {

        float frameTime = 0.333f;

        xV += (xA * frameTime);
        yV += (yA * frameTime);

        float xS = (xV / 2) * frameTime;
        float yS = (yV / 2) * frameTime;

        bX -= xS;
        bY -=yS;

        if (bX > xMax)
        {   bX = xMax;  xV = 0; }

        if (bX < 0)
        {    bX = 0;    xV = 0; }

        if (bY > yMax)
        {   bY = yMax;  yV = 0; }

        if (bY < 150)
        {   bY = 150;   yV = 0; }
    }

    ///////////////////////////getScreenWidth&Height//////////////////////////////////
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    ///////////////////////////checkLevel//////////////////////////////////
    public int checkLevel(){
        return level * 5;
    }

    ///////////////////////////AsteroidCollision//////////////////////////////////
    private boolean AsteroidCollision(int aX, int aY, int aSize){

        float centerBallX = bX + BALL_SIZE/2;
        float centerBallY = bY + BALL_SIZE/2;
        float centerAsteroidX1 = aX + aSize/2;
        float centerAsteroidY1 = aY + aSize/2;

        float xD = centerAsteroidX1 - centerBallX;
        float yD = centerAsteroidY1 - centerBallY;

        if (Math.hypot(xD,yD) <= (aSize/2) + (BALL_SIZE/2))
            return true;
        else
            return false;
    }

    ///////////////////////////MoonCollision//////////////////////////////////
    private boolean MoonCollision(int mX, int mY) {

        float centerBallX = bX + BALL_SIZE/2;
        float centerBallY = bY + BALL_SIZE/2;
        float centerMoonX = mX + MOON_SIZE/2;
        float centerMoonY = mY + MOON_SIZE/2;
        float xD = centerMoonX - centerBallX;
        float yD = centerMoonY - centerBallY;

        if (Math.hypot(xD,yD) <= (MOON_SIZE/2) + (BALL_SIZE/2))
            return true;
        else
            return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { } //not in use

    ///////////////////////////BitmapView////////////////////////////////////
    public class BitmapView extends View {


        public BitmapView (Context context)
        {
            super(context);
        }

        public Bitmap createBitmap(int id, final int width, final int height) {
            Bitmap nameSrc = BitmapFactory.decodeResource(getResources(),id);
            return Bitmap.createScaledBitmap(nameSrc, width, height, true);
        }

        public void drawMyBitmap(Canvas canvas, Bitmap bitmap, float left, float top) {
            canvas.drawBitmap(bitmap, left, top, null);
        }
    }

    ///////////////////////////StatusBarView/////////////////////////////////
    public class StatusBarView extends BitmapView {

        Bitmap background, topbar, heart, moonicon;

        public StatusBarView (Context context) {
            super(context);

            final int barHeight = 130;
            final int iconWidth = 90;
            final int iconHeight = 90;

            //Create background
            background = createBitmap(R.drawable.nightsky,getScreenWidth(),getScreenHeight());

            //Create top bar
            topbar = createBitmap(R.drawable.bar,getScreenWidth(), barHeight);

            //Create the heart icon
            heart = createBitmap(R.drawable.heart, iconWidth, iconHeight);

            //Create the moon icon
            moonicon = createBitmap(R.drawable.smallmoon, iconWidth, iconHeight);
        }

        ///////////////////////////StatusBarOnDraw/////////////////////////////////
        @Override
        protected void onDraw(Canvas canvas) {
            // Create paint
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setStrokeWidth(7);
            p.setTextSize(90);

            //Draw background
            drawMyBitmap(canvas, background, 0, 0);

            //Draw the top bar
            drawMyBitmap(canvas, topbar, 0 ,0);

            //Draw the border between the bar and screen
            canvas.drawLine(0, 130, getScreenWidth(), 130, p);

            //Draw the heart icons
            int hX = 0;
            for (int i = 0; i < heartCount; i++)
            {
                if (i == 0) hX = 30;
                    drawMyBitmap(canvas, heart, hX, 15);
                hX += 120;
            }
            canvas.drawLine(400, 0, 400, 130, p);    //Draw the border

            //Draw the moon icon
            drawMyBitmap(canvas, moonicon, 430, 15);

            //Display the score
            if (score < 10)
                canvas.drawText("0" + Integer.toString(score), 530, 95, p);
            else
                canvas.drawText(Integer.toString(score), 530, 95, p);
            canvas.drawLine(650, 0, 650, 130, p);    //Draw the border

            //Display the level
            canvas.drawText("Level: " + Integer.toString(level), 700, 95, p);
            canvas.drawLine(1005, 0, 1005, 130, p);    //Draw the border

            invalidate();
        }

    }

    ///////////////////////////MoonView/////////////////////////////////
    public class MoonView extends BitmapView {

        Bitmap moon, effect, girl;
        final int Width = 700;
        final int Height = 600;
        final int moonWidth = MOON_SIZE;
        final int moonHeight = MOON_SIZE;

        int mX = getScreenWidth()/2 - MOON_SIZE/2, mY = 150;

        public MoonView(Context context) {
            super(context);

            //Create the moon
            moon = createBitmap(R.drawable.moon, moonWidth, moonHeight);
            //Create the girl
            girl = createBitmap(R.drawable.g, GIRL_SIZE, GIRL_SIZE);
            //Create the effect
            effect = createBitmap(R.drawable.effect, Width, Height);
        }

        ///////////////////////////MoonViewOnDraw/////////////////////////////////
        @Override
        protected void onDraw(Canvas canvas) {
            //Check if the collision happens
            mCollision = MoonCollision(mX, mY);

            if (mCollision == false) {
                //Draw the moon
                drawMyBitmap(canvas, moon, mX, mY);
                invalidate();
            }
            else
            {
                if (MainActivity.soundOn)
                    sound.playMoonSound();
                else
                    MainActivity.soundOn = false;
                onStop();
                score++;
                level++;
                gainOneScore();
                //Draw the moon
                drawMyBitmap(canvas, moon, mX, mY);
                //Draw the effect
                drawMyBitmap(canvas, effect, mX - 130, mY - 80);
                //Draw the girl on the moon
                drawMyBitmap(canvas, girl, mX + MOON_SIZE/2 - GIRL_SIZE/2, mY + MOON_SIZE/2 - GIRL_SIZE/2);
                recreate();
            }

        }
    }

    ///////////////////////////AsteroidView/////////////////////////////////
    public class AsteroidView extends BitmapView {

        Bitmap asteroid, explode;
        final int Width = 600;
        final int Height = 500;
        final int aWidth = SMALL_ASTEROID_SIZE;
        final int aHeight = SMALL_ASTEROID_SIZE;
        int aX, aY;
        boolean aCollision = false;

        public AsteroidView(Context context, int _aX, int _aY) {
            super(context);

            aX = _aX;
            aY = _aY;

            //Create the asteroids
            asteroid = createBitmap(R.drawable.newasteroid, aWidth, aHeight);
            //Create the explosion
            explode = createBitmap(R.drawable.explode, Width, Height);
        }

        ///////////////////////////AsteroidViewOnDraw/////////////////////////////////
        @Override
        protected void onDraw(Canvas canvas) {

            //Check if the collision happens
            aCollision = AsteroidCollision(aX,aY,SMALL_ASTEROID_SIZE);

            //Draw the asteroids
            if (aCollision == false)
            {
                drawMyBitmap(canvas, asteroid, aX, aY);
                invalidate();
            }
            else //Display the collision
            {
                if (MainActivity.soundOn)
                    sound.playExplodeSound();
                else
                    MainActivity.soundOn = false;
                onStop();
                loseOneLife();
                heartCount--;
                drawMyBitmap(canvas, explode, bX - BALL_SIZE, bY - BALL_SIZE);
                drawMyBitmap(canvas, asteroid, aX, aY);
                recreate();
            }
        }
    }


    public class MovingAsteroidView extends AsteroidView {
        final int aWidth = ASTEROID_SIZE;
        final int aHeight = ASTEROID_SIZE;
        int aSpeed;

        public MovingAsteroidView(Context context, int _aX, int _aY) {
            super(context, _aX, _aY);

            //Create the asteroids
            asteroid = createBitmap(R.drawable.newasteroid, aWidth, aHeight);

            //Change the speed based on the level
            aSpeed = checkLevel();

        }

        public int drawAsteroid (int aX)
        {
            if (aX >= getScreenWidth()- ASTEROID_SIZE)
            {
                aX = getScreenWidth() - ASTEROID_SIZE;
                aSpeed = -aSpeed;
            }
            if (aX <= 0)
            {
                aX = 0;
                aSpeed = -aSpeed;
            }
            return aX;
        }

        ///////////////////////////MovingAsteroidViewOnDraw/////////////////////////////////
        @Override
        protected void onDraw(Canvas canvas) {

            //Check if the collision happens
            aCollision = AsteroidCollision(aX,aY,ASTEROID_SIZE);

            //Draw the asteroids
            if (aCollision == false)
            {
                aX = drawAsteroid(aX);
                aX = aX + aSpeed;
                drawMyBitmap(canvas, asteroid, aX, aY);
                invalidate();
            }
            else //Display the collision
            {
                if (MainActivity.soundOn)
                    sound.playExplodeSound();
                else
                    MainActivity.soundOn = false;
                onStop();
                loseOneLife();
                heartCount--;
                drawMyBitmap(canvas, explode, bX - BALL_SIZE, bY - BALL_SIZE);
                drawMyBitmap(canvas, asteroid, aX, aY);
                recreate();
            }
        }
    }

    ///////////////////////////BallView/////////////////////////////////
    public class BallView extends BitmapView {

        Bitmap ball, girl;
        final int ballWidth = BALL_SIZE;
        final int ballHeight = BALL_SIZE;

        public BallView(Context context) {
            super(context);

            //Create the ball
            ball = createBitmap(R.drawable.ball, ballWidth, ballHeight);
            //Create the girl
            girl = createBitmap(R.drawable.g, GIRL_SIZE, GIRL_SIZE);
        }

        ///////////////////////////BallViewOnDraw/////////////////////////////////
        @Override
        protected void onDraw(Canvas canvas) {

            if (mCollision == false)
            {
                //Draw the ball
                drawMyBitmap(canvas, ball, bX, bY);
                //Draw the girl in the ball
                drawMyBitmap(canvas, girl, bX+25, bY+25);
                invalidate();
            }
        }
    }

    ///////////////////////////Toast/////////////////////////////////
    private void loseOneLife()
    {
        Toast.makeText(this, "You have lost a life.", Toast.LENGTH_LONG).show();
    }
    private void gainOneScore()
    {
        Toast.makeText(this, "You have gained 1 point. Level Up.", Toast.LENGTH_SHORT).show();
    }
    private void startNewGame()
    {
        Toast.makeText(this, "You have started a new game.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("242", "OnStop");
        sm.unregisterListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("242", "OnStart");
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("242", "OnResume");
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("242", "OnDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("242", "OnPause");
        sm.unregisterListener(this);
    }

}
