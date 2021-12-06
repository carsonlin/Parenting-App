package Model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import ca.cmpt276.chlorinefinalproject.R;

public class TakeBreath {

    public static final int ANIMTAION_TIME = 6000;
    private static final int MAX_VOLUME = 10;
    private MediaPlayer mp;
    private MediaPlayer mpBreathing;
    private Boolean isAnimate;
    private Boolean isLoop;
    private Boolean isBreathingout;
    private Activity activity;
    private Boolean isButtonheld;
    private Button takeBreathbutton;
    private ImageView animation1;
    private ImageView animation2;
    private int secondsButtonheld = 0;
    private int breaths;
    private int breathGoal;
    private int dimentions;
    private ObjectAnimator animator1Exhale;
    private ObjectAnimator animator2Exhale;
    private ObjectAnimator animator1Inhale;
    private ObjectAnimator animator2Inhale;

    public TakeBreath(Activity activity) {
        mp = MediaPlayer.create(activity.getApplicationContext(), R.raw.in_the_light);

        float log1=(float)(Math.log(MAX_VOLUME-8)/Math.log(MAX_VOLUME));
        float log2=(float)(Math.log(MAX_VOLUME-2)/Math.log(MAX_VOLUME));

        mp.setVolume(log1,log1);


        mpBreathing = MediaPlayer.create(activity.getApplicationContext(), R.raw.better_exhale);
        mpBreathing.setVolume(log2,log2);



        mp.setLooping(true);
        setAnimate(false);
        setLoop(false);
        setBreathingout(false);
        this.activity = activity;
    }

    // Setters

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

    public void setAnimate(Boolean animate) {
        isAnimate = animate;
    }

    public void setLoop(Boolean loop) {
        isLoop = loop;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setButtonheld(Boolean buttonheld) {
        isButtonheld = buttonheld;
    }

    public void setTakeBreathbutton(Button takeBreathbutton) {
        this.takeBreathbutton = takeBreathbutton;
    }

    public void setAnimation1(ImageView animation1) {
        this.animation1 = animation1;
    }

    public void setAnimation2(ImageView animation2) {
        this.animation2 = animation2;
    }

    public void setSecondsButtonheld(int secondsButtonheld) {
        this.secondsButtonheld = secondsButtonheld;
    }

    public void setBreaths(int breaths) {
        this.breaths = breaths;
    }

    public void setBreathingout(Boolean breathingout) {
        isBreathingout = breathingout;
    }

    public void setBreathGoal(int breathGoal) {
        this.breathGoal = breathGoal;
    }
// Getters

    public void setLengthheight(int height) {
        this.dimentions = height;
    }

    public static int getAnimtaionTime() {
        return ANIMTAION_TIME;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public Boolean isAnimating() {
        return isAnimate;
    }

    public Boolean isLoop() {
        return isLoop;
    }

    public Activity getActivity() {
        return activity;
    }

    public Boolean getButtonheld() {
        return isButtonheld;
    }

    public Button getTakeBreathbutton() {
        return takeBreathbutton;
    }

    public ImageView getAnimation1() {
        return animation1;
    }

    public ImageView getAnimation2() {
        return animation2;
    }

    public int getSecondsButtonheld() {
        return secondsButtonheld;
    }

    public int getBreaths() {
        return breaths;
    }

    public Boolean getBreathingout() {
        return isBreathingout;
    }

    public int getBreathGoal() {
        return breathGoal;
    }
// Other functions

    public void playCalmingmusic(){
        mp.start();
    }

    public void stopCalmingmusic(){
        mp.stop();
    }

    public void setInhale() throws IOException {
        mpBreathing = MediaPlayer.create(activity.getApplicationContext(), R.raw.better_inhale);

        mpBreathing.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpBreathing.start();
            }
        });

        stopAllanimation();
        setAnimate(true);
        setLoop(true);
        takeBreathbutton.setText(" IN ");
        playCalmingmusic();
        float log2=(float)(Math.log(MAX_VOLUME-8)/Math.log(MAX_VOLUME));
        mpBreathing.setVolume(log2,log2);
        pulseOutloop(ANIMTAION_TIME);

    }

    public void reset(){

        suspendAnimation();
        takeBreathbutton.setText(" IN ");

    }



    public void setExhale() throws IOException {

        mpBreathing = MediaPlayer.create(activity.getApplicationContext(), R.raw.better_exhale);

        mpBreathing.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mpBreathing.start();
            }
        });

        stopAllanimation();
        setAnimate(true);
        setLoop(true);
        takeBreathbutton.setText(" OUT ");
        //stopCalmingmusic();
        float log2=(float)(Math.log(MAX_VOLUME-8)/Math.log(MAX_VOLUME));
        mpBreathing.setVolume(log2,log2);

        mpBreathing.start();
        pulseInloop(ANIMTAION_TIME);

    }

    public void suspendAnimation(){

        stopAllanimation();
        stopCalmingmusic();

    }

    public void retractCircle(){

        setAnimate(false);
        setLoop(false);
        takeBreathbutton.setText(" IN ");
        stopCalmingmusic();
        pulseInloop(ANIMTAION_TIME);

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

    public void stopAllanimation() {

        setAnimate(false);
        setLoop(false);

        if (animator1Inhale != null)
            animator1Inhale.cancel();

        if (animator2Inhale != null)
            animator2Inhale.cancel();


        if (animator1Exhale != null)
            animator1Exhale.cancel();

        if (animator2Exhale != null)
            animator2Exhale.cancel();

        resetAnimation();
    }
    private void resetAnimation(){

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", animation1.getScaleX(),1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", animation1.getScaleY(),1f);
        PropertyValuesHolder pvhYa = PropertyValuesHolder.ofFloat("alpha", animation1.getAlpha(),1f);
        animator1Exhale = ObjectAnimator.ofPropertyValuesHolder(animation1, pvhX, pvhY,pvhYa);
        animator1Exhale.setDuration(0);
        animator1Exhale.start();

        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", animation2.getScaleX(),1f);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", animation2.getScaleY(),1f);
        PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", animation2.getAlpha(),1f);
        animator2Exhale = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX, pvhY,pvhYa);
        animator2Exhale.setDuration(0);
        animator2Exhale.start();


    }

    private void pulseInloop(long animtaionTime){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 3f,1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 3f,1f);
        PropertyValuesHolder pvhYa = PropertyValuesHolder.ofFloat("alpha", 0f,1f);
        animator1Exhale = ObjectAnimator.ofPropertyValuesHolder(animation1, pvhX, pvhY,pvhYa);
        animator1Exhale.setDuration(animtaionTime);
        animator1Exhale.start();
        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", 3f, 1f);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", 3f, 1f);
        PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        animator2Exhale = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX2, pvhY2, pvhYa2);
        animator2Exhale.setDuration((long) (animtaionTime*0.8));
        animator2Exhale.setStartDelay(animtaionTime);
        animator2Exhale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if(isLoop())
                    pulseInloop(animtaionTime);

            }

        });

        animator2Exhale.start();

    }

    private void pulseOutloop(long animtaionTime){
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f,3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f,3f);
        PropertyValuesHolder pvhYa = PropertyValuesHolder.ofFloat("alpha", 1f,0f);
        animator1Inhale = ObjectAnimator.ofPropertyValuesHolder(animation1, pvhX, pvhY,pvhYa);
        animator1Inhale.setDuration(animtaionTime);
        animator1Inhale.start();
        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 3f);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 3f);
        PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        animator2Inhale = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX2, pvhY2, pvhYa2);
        animator2Inhale.setDuration((long) (animtaionTime * 0.8));
        animator2Inhale.setStartDelay(animtaionTime);
        animator2Inhale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if(isLoop())
                    pulseOutloop(animtaionTime);

            }
        });

        animator2Inhale.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float v) {

                System.out.println(v);

                return 0;
            }
        });

        animator2Inhale.start();


    }



}
