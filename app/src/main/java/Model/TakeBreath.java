package Model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import ca.cmpt276.chlorinefinalproject.R;

public class TakeBreath {
    public static final int ANIMATION_TIME = 10000;
    private static final int MAX_VOLUME = 10;
    private MediaPlayer mp = new MediaPlayer();
    private MediaPlayer mpBreathing;
    private Boolean isAnimate;
    private Boolean isLoop;
    private Boolean isBreathingOut;
    private Activity activity;
    private Boolean isButtonHeld;
    private Button takeBreathButton;
    private ImageView animation1;
    private ImageView animation2;
    private int secondsButtonHeld = 0;
    private int breaths;
    private int breathGoal;
    private boolean stop = false;
    private ObjectAnimator animator1Exhale;
    private ObjectAnimator animator2Exhale;
    private ObjectAnimator animator1Inhale;
    private ObjectAnimator animator2Inhale;

    public TakeBreath(Activity activity) {


        //float log1 = (float)(Math.log(MAX_VOLUME-8)/Math.log(MAX_VOLUME));
        //float log2 = (float)(Math.log(MAX_VOLUME-2)/Math.log(MAX_VOLUME));
        //mp.setVolume(log1,log1);
        //mpBreathing = MediaPlayer.create(activity.getApplicationContext(), R.raw.exhale);
        //mpBreathing.setVolume(log2,log2);
       // mp.setLooping(true);
        setAnimate(false);
        setLoop(false);
        setBreathingOut(false);
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

    public void setButtonHeld(Boolean buttonHeld) {
        isButtonHeld = buttonHeld;
    }

    public void setTakeBreathButton(Button takeBreathButton) {
        this.takeBreathButton = takeBreathButton;
    }

    public void setAnimation1(ImageView animation1) {
        this.animation1 = animation1;
    }

    public void setAnimation2(ImageView animation2) {
        this.animation2 = animation2;
    }

    public void setSecondsButtonHeld(int secondsButtonheld) {
        this.secondsButtonHeld = secondsButtonheld;
    }

    public void setBreaths(int breaths) {
        this.breaths = breaths;
    }

    public void setBreathingOut(Boolean breathingout) {
        isBreathingOut = breathingout;
    }

    public void setBreathGoal(int breathGoal) {
        this.breathGoal = breathGoal;
    }
// Getters

    public void setLengthHeight(int height) {
    }

    public static int getAnimationTime() {
        return ANIMATION_TIME;
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

    public Boolean getButtonHeld() {
        return isButtonHeld;
    }

    public Button getTakeBreathButton() {
        return takeBreathButton;
    }

    public ImageView getAnimation1() {
        return animation1;
    }

    public ImageView getAnimation2() {
        return animation2;
    }

    public int getSecondsButtonHeld() {
        return secondsButtonHeld;
    }

    public int getBreaths() {
        return breaths;
    }

    public Boolean getBreathingOut() {
        return isBreathingOut;
    }

    public int getBreathGoal() {
        return breathGoal;
    }

// Other functions
    public void playCalmingMusic(){
        float logVol=(float)(Math.log(MAX_VOLUME-8)/Math.log(MAX_VOLUME));
            stopAndPlay(R.raw.calm, mp);
            mp.setLooping(false);
            mp.setVolume(logVol,logVol);
            stop = false;
    }

    public void stopCalmingMusic() throws IOException {
        if (!stop) {
            mp.reset();
            mp.release();
            stop = true;
       }
    }

    private void stopAndPlay(int rawId, MediaPlayer mediaPlayer) {
        mediaPlayer.reset();
        AssetFileDescriptor afd = getActivity().getResources().openRawResourceFd(rawId);
        try {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }


    public void setInhale() {
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.circle).into(animation1);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.circle).into(animation2);
        takeBreathButton.setBackground(getActivity().getDrawable(R.drawable.circle));
        mpBreathing = MediaPlayer.create(activity.getApplicationContext(), R.raw.inhale);
        mpBreathing.setOnPreparedListener(mp -> mpBreathing.start());
        stopAllAnimation();
        setAnimate(true);
        setLoop(true);
        playCalmingMusic();
        float log2=(float)(Math.log(MAX_VOLUME-2)/Math.log(MAX_VOLUME));
        mpBreathing.setVolume(log2,log2);
        pulseOutLoop(ANIMATION_TIME);
    }

    public void reset(){
        suspendAnimation();
    }

    public void setExhale() {
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.green_circle).into(animation1);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.green_circle).into(animation2);
        takeBreathButton.setBackground(getActivity().getDrawable(R.drawable.green_circle));
        mpBreathing = MediaPlayer.create(activity.getApplicationContext(), R.raw.exhale);
        mpBreathing.setOnPreparedListener(mp -> mpBreathing.start());
        stopAllAnimation();
        setAnimate(true);
        setLoop(true);
        float log2=(float)(Math.log(MAX_VOLUME-2)/Math.log(MAX_VOLUME));
        mpBreathing.setVolume(log2,log2);
        mpBreathing.start();
        pulseInLoop(ANIMATION_TIME);

    }

    public void suspendAnimation(){
        setLoop(false);
        setAnimate(false);
        try {
            stopCalmingMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retractCircle(){
        setAnimate(false);
        setLoop(false);
        stopAllAnimation();
        mpBreathing.stop();
        try {
            stopCalmingMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopAllAnimation() {
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
        animator1Exhale.setDuration(50);
        animator1Exhale.start();

        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", animation2.getScaleX(),1f);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", animation2.getScaleY(),1f);
        PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", animation2.getAlpha(),1f);
        animator2Exhale = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX2, pvhY2, pvhYa2);
        animator2Exhale.setDuration(50);
        animator2Exhale.start();
    }

    private void pulseInLoop(long animationTime){
        stopAllAnimation();
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 3f,1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 3f,1f);
        PropertyValuesHolder pvhYa = PropertyValuesHolder.ofFloat("alpha", 0f,1f);
        animator1Exhale = ObjectAnimator.ofPropertyValuesHolder(animation1, pvhX, pvhY,pvhYa);
        animator1Exhale.setDuration(animationTime);
        animator1Exhale.start();
        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", 3f, 1f);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", 3f, 1f);
        PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", 0f, 1f);
        animator2Exhale = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX2, pvhY2, pvhYa2);
        animator2Exhale.setDuration((long) (animationTime*0.8));
        animator2Exhale.setStartDelay(animationTime);
        animator2Exhale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(isLoop())
                    pulseInLoop(animationTime);
            }

        });

        animator2Exhale.start();

    }

    private void pulseOutLoop(long animationTime){
        stopAllAnimation();
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f,3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f,3f);
        PropertyValuesHolder pvhYa = PropertyValuesHolder.ofFloat("alpha", 1f,0f);
        animator1Exhale = ObjectAnimator.ofPropertyValuesHolder(animation1, pvhX, pvhY,pvhYa);
        animator1Exhale.setDuration(animationTime);
        animator1Exhale.start();
        PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 3f);
        PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 3f);
        PropertyValuesHolder pvhYa2 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);
        animator2Exhale = ObjectAnimator.ofPropertyValuesHolder(animation2, pvhX2, pvhY2, pvhYa2);
        animator2Exhale.setDuration((long) (animationTime * 0.8));
        animator2Exhale.setStartDelay(animationTime);
        animator2Exhale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if(isLoop())
                    pulseOutLoop(animationTime);

            }
        });
        animator2Exhale.start();
    }
}
