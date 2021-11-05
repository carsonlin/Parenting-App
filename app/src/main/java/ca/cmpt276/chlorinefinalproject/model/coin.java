package ca.cmpt276.chlorinefinalproject.model;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

import ca.cmpt276.chlorinefinalproject.R;


public class coin {

    ImageView coin;

    float rotation = 0;

    int flip = 0;

    boolean head = true;

    private int stop = 0;

    private Activity context;

    private int lowerbound = 20;

    private int upperboound = 40;

    private int coinChangeangle = 90;

    public coin(Activity context, ImageView coin){

        this.context = context;

        this.coin = coin;

    }

    public flipoutcome flip(){

        // generate random stoping point every time coin is flipped
        genearateRandom(lowerbound,upperboound);

        MediaPlayer mp = MediaPlayer.create(this.context, R.raw.coin_flip);
        mp.start();

        flipanimation();

        flipoutcome outcome = new flipoutcome(head,(100 * stop));


        return outcome;
    }

    private void genearateRandom(int startOfRange, int endOfRange){

        Random rand = new Random();

        int randomNum = rand.nextInt(100/2) *2;

        stop = startOfRange+rand.nextInt((endOfRange-startOfRange)/2) *2;

    }

    /*
     *
     * flips image
     * at every 90 mulitple images are alternated
     *
     * */
    public void flipanimation() {

        ObjectAnimator animation = ObjectAnimator.ofFloat(coin, "rotationX", rotation, (rotation + 90));

        // arbitary 100, was a sweet spot
        animation.setDuration(100);

        animation.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                rotation += coinChangeangle;

                flip+=1;

                if ((flip % 2) == 1) {

                    if (head) {
                        Glide.with(context).load(R.drawable.loony_tail).into(coin);
                    }else {
                        Glide.with(context).load(R.drawable.head_loony).into(coin);
                    }
                    head = !head;

                }

                if (flip<stop)
                    flipanimation();
                else {
                    flip = 0;
                    genearateRandom(lowerbound, upperboound);
                }
            }
        });

        animation.start();


    }

}
