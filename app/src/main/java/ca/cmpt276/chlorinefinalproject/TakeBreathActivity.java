package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class TakeBreathActivity extends AppCompatActivity {


    public static final int ANIMTAION_TIME = 6000;
    private boolean statusAnimation = false;
    private Button takeBreathbutton;
    private ImageView animation1;
    private ImageView animation2;
    private Boolean isIdle = true;
    private int secondsButtonheld = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        takeBreathbutton = findViewById(R.id.takeBreathbutton);
        animation1 = findViewById(R.id.imgAnimation1);
        animation2 = findViewById(R.id.imgAnimation1);

        //pulseOut(5000, false);
        pulseOutloop(5000,false);

        takeBreathbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Do something
                        isIdle = false;
                        final long delay= pulseWaittime(ANIMTAION_TIME,false);
                        pulseAnimationout(ANIMTAION_TIME,false);
                        trackButtonheldTime();
                        return true;
                    case MotionEvent.ACTION_UP:
                        // No longer down
                        isIdle = true;
/*
                        if (secondsButtonheld>=10)
                            pulseAnimationin(ANIMTAION_TIME,false);
*/
                        return true;
                }
                return false;
            }
        });

    }

    private void trackButtonheldTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {

                if (isIdle) {
                    System.out.println("???????????????????????????????????");

                    if((secondsButtonheld>=3)&&(secondsButtonheld<10)){
                        pulseAnimationin(ANIMTAION_TIME,false);
                    }

                }else{

                    if ((secondsButtonheld>=3)){
                        takeBreathbutton.setText(" OUT ");
                    }else if ((secondsButtonheld>=10)){
                        takeBreathbutton.setText(" OUT ");
                    }

                    secondsButtonheld+=1;
                }
            System.out.println(" seconds held "+secondsButtonheld);
                if (!isIdle)
                handler.postDelayed(this,1000);

            }
            },  1000);
    }

    private void pulseAnimationout(long animtaionTime, boolean liveAnimtation) {
        Handler handler = new Handler();
        long delayTime = pulseWaittime(animtaionTime, liveAnimtation);
        pulseOut(animtaionTime, liveAnimtation);
        handler.postDelayed(new Runnable(){
            public void run() {
                //if (isIdle) {
                pulseOut(animtaionTime, liveAnimtation);
                    handler.postDelayed(this, delayTime);
                //}
            }
        },  delayTime);
    }

    private void pulseAnimationin(long animtaionTime,boolean liveAnimtation) {
        Handler handler = new Handler();
        long delayTime = pulseWaittime(animtaionTime, liveAnimtation);
        pulseIn(animtaionTime, liveAnimtation);
        handler.postDelayed(new Runnable(){
            public void run() {
               // if (isIdle) {
                pulseIn(animtaionTime, liveAnimtation);
                handler.postDelayed(this, animtaionTime);
                //}
            }
        },  delayTime);
    }

    private void pulseIn(long animtaionTime,boolean liveAnimtation){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 3f,1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 3f,1f);
        PropertyValuesHolder pvhYa = PropertyValuesHolder.ofFloat("alpha", 0f,1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animation1, pvhX, pvhY,pvhYa);
        animator.setDuration(animtaionTime);
        animator.start();
        if (liveAnimtation) {
            PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", 3f, 1f);
            PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", 3f, 1f);
            PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
            ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX2, pvhY2, pvhYa2);
            animator2.setDuration((long) (animtaionTime*0.8));
            animator2.setStartDelay(animtaionTime);
            animator2.start();
        }
    }

    private void pulseOut(long animtaionTime,boolean liveAnimtation){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f,3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f,3f);
        PropertyValuesHolder pvhYa = PropertyValuesHolder.ofFloat("alpha", 1f,0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animation1, pvhX, pvhY,pvhYa);
        animator.setDuration(animtaionTime);
        animator.start();
        //if (liveAnimtation) {
            PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 3f);
            PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 3f);
            PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
            ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX2, pvhY2, pvhYa2);
            animator2.setDuration((long) (animtaionTime * 0.8));
            animator2.setStartDelay(animtaionTime);
            animator2.start();
        //}

    }

    private void pulseOutloop(long animtaionTime,boolean liveAnimtation){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f,3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f,3f);
        PropertyValuesHolder pvhYa = PropertyValuesHolder.ofFloat("alpha", 1f,0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animation1, pvhX, pvhY,pvhYa);
        animator.setDuration(animtaionTime);
        animator.start();
        //if (liveAnimtation) {
        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 3f);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 3f);
        PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX2, pvhY2, pvhYa2);
        animator2.setDuration((long) (animtaionTime * 0.8));
        animator2.setStartDelay(animtaionTime);
        animator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pulseOutloop(animtaionTime,liveAnimtation);
            }
        });
        animator2.start();
        //}

    }

    private long pulseWaittime(long animtaionTime,boolean liveAnimtation){
        animtaionTime+=(liveAnimtation?0:(animtaionTime*0.8));
        return animtaionTime;

    }


}