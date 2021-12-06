package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import Model.TakeBreath;

public class TakeBreathActivity extends AppCompatActivity {

    private Button takeBreathbutton;
    private int secondsButtonheld = 0;
    private int secondsButtonreleased = 0;
    private TakeBreath takeBreath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        takeBreath = new TakeBreath(TakeBreathActivity.this);
        takeBreathbutton = findViewById(R.id.takeBreathbutton);
        takeBreath.setTakeBreathbutton(findViewById(R.id.takeBreathbutton));
        takeBreath.setAnimation1(findViewById(R.id.imgAnimation1));
        takeBreath.setAnimation2(findViewById(R.id.imgAnimation2));
        takeBreathbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Do something, Button is held

                        takeBreath.setButtonheld(true);
                        takeBreath.setAnimate(false);
                        trackButtonHeldTime();
                        return true;
                    case MotionEvent.ACTION_UP:
                        // Button is No longer down
                        takeBreath.setButtonheld(false);
                        return true;
                }
                return false;
            }
        });

    }

    private void trackButtonHeldTime() {

        takeBreath.setInhale();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {

                takeBreath.setBreathingout(false);
                secondsButtonheld+=1;

                if (takeBreath.getButtonheld()){

                    if ((secondsButtonheld>=3)&&(secondsButtonheld<10)){

                        if (!takeBreath.isAnimating()){
                            {
                                System.out.println(" -- inhale -- ");
                                takeBreath.setInhale();
                            }
                        }

                    }else if ((secondsButtonheld>=10)){
                        // Release button for animation
                        takeBreath.suspendAnimation();
                    }



                }
                else{


                    if ((secondsButtonheld<3)){

                        System.out.println(" -- not enough -- ");

                        takeBreath.retractCircle();

                    }else if ((secondsButtonheld>=3)&&(secondsButtonheld<10)){

                        //takeBreath.setAnimate(false);
                        System.out.println(" -- btn -- ");
                        trackButtonReleasedTime();

                    }else{
                        System.out.println(" -- too long -- ");
                        takeBreath.suspendAnimation();
                        trackButtonReleasedTime();


                    }

                    secondsButtonheld = 0;
                }

                System.out.println(" seconds held "+secondsButtonheld);

                if (takeBreath.getButtonheld())
                    handler.postDelayed(this,1000);

            }
            },  1000);
    }

    private void trackButtonReleasedTime() {
        takeBreath.setExhale();
        takeBreath.setBreathingout(true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {

                secondsButtonreleased+=1;

                if (!takeBreath.getButtonheld()){

                    if ((secondsButtonreleased>=3)&&(secondsButtonreleased<10)){

                        takeBreath.getTakeBreathbutton().setText(" IN ");
                        if (!takeBreath.isAnimating()){
                            takeBreath.setExhale();

                        }

                        if (takeBreath.getBreathingout()) {
                            takeBreath.setBreathingout(true);
                            // Release button for animation and change state
                            int currentBreathcount = takeBreath.getBreaths();
                            if (currentBreathcount < 10) {
                                takeBreath.setBreaths(currentBreathcount + 1);
                            } else if (currentBreathcount == 1)
                                takeBreath.playCalmingmusic(); //  should congratualte user

                        }
                    }else if ((secondsButtonreleased>=10)){
                        // Release button for animation
                        takeBreath.setLoop(false);

                    }

                }else {
                    secondsButtonreleased = 0;
                }


                System.out.println(" seconds released "+secondsButtonreleased);

                if (!takeBreath.getButtonheld()&&takeBreath.isAnimating())
                    handler.postDelayed(this,1000);

            }
        },  1000);
    }


}