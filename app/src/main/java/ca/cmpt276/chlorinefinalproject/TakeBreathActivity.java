package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import Model.TakeBreath;

public class TakeBreathActivity extends AppCompatActivity {

    public static final String BREATHS = "breaths";
    private Button takeBreathbutton;
    private Button navigateTobreathButton;
    private EditText breathCounteditText;
    private View card_view_pickBreaths;
    private TextView stateDescriber;
    private ImageView minus;
    private ImageView plus;
    private ImageView animate1;
    private ImageView animate2;
    private int secondsButtonheld = 0;
    private int secondsButtonreleased = 0;
    private TakeBreath takeBreath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        takeBreath = new TakeBreath(TakeBreathActivity.this);
        setUpui();
        setBreathgoalViewinteractions();

        if (isBreathgoalView())
            setBreathGoalView();
        else
            setBreathanimationView();

        takeBreathbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Do something, Button is held

                        takeBreath.setButtonheld(true);
                        takeBreath.setAnimate(false);
                        try {
                            trackButtonHeldTime();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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

    private void setUpui() {
        takeBreathbutton = findViewById(R.id.takeBreathbutton);
        minus = findViewById(R.id.minusImage);
        plus = findViewById(R.id.plusImage);
        breathCounteditText = findViewById(R.id.editTextNumberBreaths);
        card_view_pickBreaths = findViewById(R.id.card_view_pickBreaths);
        stateDescriber = findViewById(R.id.stateDescriber);
        animate1 = findViewById(R.id.imgAnimation1);
        animate2 = findViewById(R.id.imgAnimation2);
        navigateTobreathButton = findViewById(R.id.startButton);
        takeBreath.setTakeBreathbutton(takeBreathbutton);
        takeBreath.setAnimation1(animate1);
        takeBreath.setAnimation2(animate2);
        takeBreath.setLengthheight(takeBreathbutton.getHeight());
    }

    private void trackButtonHeldTime() throws IOException {

        takeBreath.setInhale();
        Glide.with(getApplicationContext()).load(R.drawable.circle).into(animate1);
        Glide.with(getApplicationContext()).load(R.drawable.circle).into(animate2);
        takeBreathbutton.setBackground(getDrawable(R.drawable.circle));
        stateDescriber.setText(" Take a deep Breath ");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                takeBreath.setBreathingout(false);
                secondsButtonheld += 1;

                if (takeBreath.getButtonheld()) {

                    if ((secondsButtonheld >= 3) && (secondsButtonheld < 10)) {

                        if (!takeBreath.isAnimating()) {
                            {
                                try {
                                    takeBreath.setInhale();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } else if ((secondsButtonheld >= 10)) {
                        // Release button for animation
                        takeBreath.suspendAnimation();
                    }

                } else {

                    if ((secondsButtonheld < 3)) {

                        takeBreath.retractCircle();

                    } else if ((secondsButtonheld >= 3) && (secondsButtonheld < 10)) {

                        try {
                            trackButtonReleasedTime();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {

                        takeBreath.suspendAnimation();
                        try {
                            trackButtonReleasedTime();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    secondsButtonheld = 0;
                }

                System.out.println(" seconds held " + secondsButtonheld);

                if (takeBreath.getButtonheld())
                    handler.postDelayed(this, 1000);

            }
        }, 1000);
    }

    private void setBreathgoalViewinteractions() {

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int minus = Integer.valueOf(breathCounteditText.getText().toString()) - 1;

                if (minus < 1)
                    breathCounteditText.setText("1");
                else
                    breathCounteditText.setText(String.valueOf(minus));

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int addition = Integer.valueOf(breathCounteditText.getText().toString()) + 1;

                if (addition > 10)
                    breathCounteditText.setText("10");
                else
                    breathCounteditText.setText(String.valueOf(addition));

            }
        });


        navigateTobreathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked");
                navigateTobreath();
            }
        });

    }

    private void trackButtonReleasedTime() throws IOException {

        takeBreath.setExhale();
        stateDescriber.setText(" Exhale ");
        takeBreath.setBreathingout(true);
        Glide.with(getApplicationContext()).load(R.drawable.green_circle).into(animate1);
        Glide.with(getApplicationContext()).load(R.drawable.green_circle).into(animate2);
        takeBreathbutton.setBackground(getDrawable(R.drawable.green_circle));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                secondsButtonreleased += 1;

                if (!takeBreath.getButtonheld()) {

                    if ((secondsButtonreleased >= 3) && (secondsButtonreleased < 10)) {

                        takeBreath.getTakeBreathbutton().setText(" IN ");


                        if (takeBreath.getBreathingout()) {

                            takeBreath.setBreathingout(true);
                            // Release button for animation and change state
                            int currentBreathcount = takeBreath.getBreaths();
                            if (currentBreathcount < takeBreath.getBreathGoal()) {
                                takeBreath.setBreaths(currentBreathcount + 1);
                            } else if (currentBreathcount == takeBreath.getBreathGoal()) {
                                stateDescriber.setText(" Good job ");
                            }
                        }
                        
                    } else if ((secondsButtonreleased >= 10)) {
                        // Release button for animation
                        takeBreath.setLoop(false);
                        takeBreath.stopCalmingmusic();

                    }

                } else {
                    secondsButtonreleased = 0;
                }

                if (!takeBreath.getButtonheld() && takeBreath.isAnimating())
                    handler.postDelayed(this, 1000);

            }
        }, 1000);
    }

    private void setBreathGoalView() {

        hideAllui();
        navigateTobreathButton.setVisibility(View.VISIBLE);
        card_view_pickBreaths.setVisibility(View.VISIBLE);
        stateDescriber.setVisibility(View.VISIBLE);
        plus.setVisibility(View.VISIBLE);
        minus.setVisibility(View.VISIBLE);

    }

    private void setBreathanimationView() {

        hideAllui();
        stateDescriber.setVisibility(View.VISIBLE);
        takeBreathbutton.setVisibility(View.VISIBLE);
        animate1.setVisibility(View.VISIBLE);
        animate2.setVisibility(View.VISIBLE);

    }

    private void hideAllui() {

        navigateTobreathButton.setVisibility(View.GONE);
        card_view_pickBreaths.setVisibility(View.GONE);
        stateDescriber.setVisibility(View.GONE);
        plus.setVisibility(View.GONE);
        minus.setVisibility(View.GONE);
        takeBreathbutton.setVisibility(View.GONE);
        animate1.setVisibility(View.GONE);
        animate2.setVisibility(View.GONE);

    }

    private boolean isBreathgoalView() {
        Intent intent = getIntent();
        if (intent.getStringExtra(BREATHS) == null)
            return true;

        return false;
    }

    private void extractIntentExtras() {
        Intent intent = getIntent();
        takeBreath.setBreathGoal(Integer.valueOf(intent.getStringExtra(BREATHS)));
    }

    private void navigateTobreath() {
        Intent intent = new Intent(getBaseContext(), TakeBreathActivity.class);
        intent.putExtra(BREATHS, breathCounteditText.getText().toString());
        startActivity(intent);
        finish();
    }


}