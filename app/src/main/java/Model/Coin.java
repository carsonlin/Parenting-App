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

    ImageView coin;
    float rotation;
    int flip;
    boolean head = true;
    private int stop;
    private boolean animating;
    private boolean interacted;
    private final Activity context;

    public Coin(Activity context, ImageView coin){
        this.context = context;
        this.coin = coin;
        this.interacted = false;
    }

    public boolean isAnimating() {
        return animating;
    }

    public boolean isHead() {
        return head;
    }

    public boolean isInteracted() {
        return interacted;
    }

    public void flip(){
        // generate random stopping point every time Coin is flipped
        generateRandom(LOWER_BOUND, UPPER_BOUND);

        MediaPlayer mp = MediaPlayer.create(this.context, R.raw.coin_flip);
        mp.start();

        flipAnimation();
    }

    private void generateRandom(int startOfRange, int endOfRange){
        Random rand = new Random();
        stop = startOfRange + (rand.nextInt((endOfRange-startOfRange)/2) * 2);
    }

    // flips image at every 90 multiple images are alternated
    public void flipAnimation() {
        interacted = true;
        animating = true;
        ObjectAnimator animation = ObjectAnimator.ofFloat(coin, "rotationX", rotation, (rotation + 90));
        // arbitrary 100, was a sweet spot
        animation.setDuration(100);
        animation.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                rotation += COIN_CHANGE_ANGLE;
                flip += 1;
                if ((flip % 2) == 1) {
                    if (head) {
                        Glide.with(context).load(R.drawable.loonie_tail).into(coin);
                    }
                    else {
                        Glide.with(context).load(R.drawable.loonie_head).into(coin);
                    }
                    head = !head;
                }

                if (flip < stop)
                    flipAnimation();
                else {
                    flip = 0;
                    animating = false;
                    generateRandom(LOWER_BOUND, UPPER_BOUND);
                }
            }
        });
        animation.start();
    }
}
