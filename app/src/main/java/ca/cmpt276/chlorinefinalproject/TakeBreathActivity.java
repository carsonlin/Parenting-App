package ca.cmpt276.chlorinefinalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;

import Model.TakeBreath;

public class TakeBreathActivity extends AppCompatActivity {

    public static final String BREATHS = "breaths";
    private Button takeBreathButton;
    private Button navigateToBreathButton;
    private EditText breathCountEditText;
    private View card_view_pickBreaths;
    private TextView stateDescriber;
    private ImageView minus;
    private ImageView plus;
    private ImageView animate1;
    private ImageView animate2;
    private int secondsButtonHeld = 0;
    private int secondsButtonReleased = 0;
    private TakeBreath takeBreath;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        takeBreath = new TakeBreath(TakeBreathActivity.this);
        setUpUI();
        setBreathGoalViewInteractions();

        if (isBreathGoalView())
            setBreathGoalView();
        else
            setBreathAnimationView();

        takeBreathButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Do something, Button is held

                    takeBreath.setButtonHeld(true);
                    takeBreath.setAnimate(false);
                    try {
                        trackButtonHeldTime();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    // Button is No longer down
                    takeBreath.setButtonHeld(false);
                    return true;
            }
            return false;
        });
    }

    private void setUpUI() {
        takeBreathButton = findViewById(R.id.takeBreathbutton);
        minus = findViewById(R.id.minusImage);
        plus = findViewById(R.id.plusImage);
        breathCountEditText = findViewById(R.id.editTextNumberBreaths);
        card_view_pickBreaths = findViewById(R.id.card_view_pickBreaths);
        stateDescriber = findViewById(R.id.stateDescriber);
        animate1 = findViewById(R.id.imgAnimation1);
        animate2 = findViewById(R.id.imgAnimation2);
        navigateToBreathButton = findViewById(R.id.startButton);
        takeBreath.setTakeBreathButton(takeBreathButton);
        takeBreath.setAnimation1(animate1);
        takeBreath.setAnimation2(animate2);
        takeBreath.setLengthHeight(takeBreathButton.getHeight());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void trackButtonHeldTime() throws IOException {
        takeBreath.setInhale();
        Glide.with(getApplicationContext()).load(R.drawable.circle).into(animate1);
        Glide.with(getApplicationContext()).load(R.drawable.circle).into(animate2);
        takeBreathButton.setBackground(getDrawable(R.drawable.circle));
        stateDescriber.setText(" Take a deep Breath ");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                takeBreath.setBreathingOut(false);
                secondsButtonHeld += 1;

                if (takeBreath.getButtonHeld()) {
                    if ((secondsButtonHeld >= 3) && (secondsButtonHeld < 10)) {
                        if (!takeBreath.isAnimating()) {
                            {
                                try {
                                    takeBreath.setInhale();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } else if ((secondsButtonHeld >= 10)) {
                        // Release button for animation
                        takeBreath.suspendAnimation();
                    }

                } else {
                    if ((secondsButtonHeld < 3)) {
                        takeBreath.retractCircle();
                    }
                    else if (secondsButtonHeld < 10) {
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
                    secondsButtonHeld = 0;
                }
                System.out.println(" seconds held " + secondsButtonHeld);

                if (takeBreath.getButtonHeld())
                    handler.postDelayed(this, 1000);

            }
        }, 1000);
    }

    private void setBreathGoalViewInteractions() {
        minus.setOnClickListener(view -> {
            int minus = Integer.parseInt(breathCountEditText.getText().toString()) - 1;
            if (minus < 1){
                breathCountEditText.setText("1");
            }
            else{
                breathCountEditText.setText(String.valueOf(minus));
            }
        });

        plus.setOnClickListener(view -> {
            int addition = Integer.parseInt(breathCountEditText.getText().toString()) + 1;
            if (addition > 10){
                breathCountEditText.setText("10");
            }
            else{
                breathCountEditText.setText(String.valueOf(addition));
            }
        });

        navigateToBreathButton.setOnClickListener(view -> {
            System.out.println("clicked");
            navigateToBreath();
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void trackButtonReleasedTime() throws IOException {
        takeBreath.setExhale();
        stateDescriber.setText(" Exhale ");
        takeBreath.setBreathingOut(true);
        Glide.with(getApplicationContext()).load(R.drawable.green_circle).into(animate1);
        Glide.with(getApplicationContext()).load(R.drawable.green_circle).into(animate2);
        takeBreathButton.setBackground(getDrawable(R.drawable.green_circle));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                secondsButtonReleased += 1;

                if (!takeBreath.getButtonHeld()) {
                    if ((secondsButtonReleased >= 3) && (secondsButtonReleased < 10)) {
                        takeBreath.getTakeBreathButton().setText(" IN ");

                        if (takeBreath.getBreathingOut()) {
                            takeBreath.setBreathingOut(true);
                            // Release button for animation and change state
                            int currentBreathcount = takeBreath.getBreaths();
                            if (currentBreathcount < takeBreath.getBreathGoal()) {
                                takeBreath.setBreaths(currentBreathcount + 1);
                            }
                            else if (currentBreathcount == takeBreath.getBreathGoal()){
                                takeBreath.playCalmingMusic(); //  should congratualte user
                            }
                        }
                    }
                    else if ((secondsButtonReleased >= 10)) {
                        // Release button for animation
                        takeBreath.setLoop(false);
                        takeBreath.stopCalmingMusic();
                    }
                }
                else {
                    secondsButtonReleased = 0;
                }

                if (!takeBreath.getButtonHeld() && takeBreath.isAnimating())
                    handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void setBreathGoalView() {
        hideAllUI();
        navigateToBreathButton.setVisibility(View.VISIBLE);
        card_view_pickBreaths.setVisibility(View.VISIBLE);
        stateDescriber.setVisibility(View.VISIBLE);
        plus.setVisibility(View.VISIBLE);
        minus.setVisibility(View.VISIBLE);
    }

    private void setBreathAnimationView() {
        hideAllUI();
        stateDescriber.setVisibility(View.VISIBLE);
        takeBreathButton.setVisibility(View.VISIBLE);
        animate1.setVisibility(View.VISIBLE);
        animate2.setVisibility(View.VISIBLE);
    }

    private void hideAllUI() {
        navigateToBreathButton.setVisibility(View.GONE);
        card_view_pickBreaths.setVisibility(View.GONE);
        stateDescriber.setVisibility(View.GONE);
        plus.setVisibility(View.GONE);
        minus.setVisibility(View.GONE);
        takeBreathButton.setVisibility(View.GONE);
        animate1.setVisibility(View.GONE);
        animate2.setVisibility(View.GONE);
    }

    private boolean isBreathGoalView() {
        Intent intent = getIntent();
        return intent.getStringExtra(BREATHS) == null;
    }

    private void extractIntentExtras() {
        Intent intent = getIntent();
        takeBreath.setBreathGoal(Integer.parseInt(intent.getStringExtra(BREATHS)));
    }

    private void navigateToBreath() {
        Intent intent = new Intent(getBaseContext(), TakeBreathActivity.class);
        intent.putExtra(BREATHS, breathCountEditText.getText().toString());
        startActivity(intent);
        finish();
    }


}