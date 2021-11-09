package Model;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Random;

import ca.cmpt276.chlorinefinalproject.R;


public class Coin {
    private static final int LOWER_BOUND = 20;
    private static final int UPPER_BOUND = 40;
    private static final int COIN_CHANGE_ANGLE = 90;
    public static final int DURATION = 100;

    ImageView coin;
    float rotation;
    int flip;
    boolean head = true;
    private int stop;
    private Boolean abortAnimation;
    private boolean animating;
    private boolean interacted;
    private final Activity context;

    public Coin(Activity context, ImageView coin){
        this.context = context;
        this.coin = coin;
        this.interacted = false;
        this.abortAnimation = false;
    }

    public boolean isAnimating() {
        return animating;
    }

    public boolean isHead() {
        return head;
    }

    public void setAbortAnimation(Boolean abortAnimation) {
        this.abortAnimation = abortAnimation;
    }

    public boolean isInteracted() {
        return interacted;
    }

    public void flip(){
        // generate random stopping point every time Coin is flipped
        generateRandomEnd();
        MediaPlayer mp = MediaPlayer.create(this.context, R.raw.coin_flip);
        mp.start();
        flipAnimation();
    }

    private void generateRandomEnd(){
        Random rand = new Random();
        stop = Coin.LOWER_BOUND + (rand.nextInt((Coin.UPPER_BOUND - Coin.LOWER_BOUND)/2) * 2);
    }

    public void flipAnimation() {
        interacted = true;
        animating = true;
        if(!this.abortAnimation){
            ObjectAnimator animation = ObjectAnimator.ofFloat(coin, "rotationX", rotation, (rotation + 90));
            // arbitrary 100, was a sweet spot
            animation.setDuration(DURATION);
            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rotation += COIN_CHANGE_ANGLE;
                    flip += 1;
                    if ((flip % 2) == 1) {
                        if (head) {
                            Glide.with(context).load(R.drawable.loonie_tail).into(coin);
                        } else {
                            Glide.with(context).load(R.drawable.loonie_head).into(coin);
                        }
                        head = !head;
                    }
                    if (flip < stop){
                        flipAnimation();
                    }
                    else {
                        flip = 0;
                        animating = false;
                        generateRandomEnd();
                    }
                }
            });
            animation.start();
        }
    }
}
