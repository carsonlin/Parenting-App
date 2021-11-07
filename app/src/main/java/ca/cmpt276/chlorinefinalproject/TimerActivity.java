package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    public static final int NUM_MILLIS_IN_SECOND = 1000;
    public static final int NUM_SECONDS_IN_MINUTE = 60;
    private CountDownTimer timer;
    private TextView timerText;
    private boolean isTimerPaused;
    private long timerDurationInMillis;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerText = findViewById(R.id.timer_text_view);
        timerText.setText(getString(R.string.timer_textview, 0, 0));

        setUpTimerButtons();
        setUpDurationRadioButtons();
        setComponentVisibility(false);
    }

    private void setUpDurationRadioButtons() {
        RadioGroup group = findViewById(R.id.timer_duration_radiogroup);
        int[] durations = getResources().getIntArray(R.array.timer_durations);

        for (int minutes : durations) {
            RadioButton btn = new RadioButton(this);
            btn.setText(getString(R.string.timer_radio_button_text, minutes));

            btn.setOnClickListener(view -> timerDurationInMillis = convertMinutesToMillis(minutes));

            group.addView(btn);
        }

        // Add custom duration input radio button
        RadioButton customBtn = new RadioButton(this);
        customBtn.setText("Custom");

        customBtn.setOnClickListener(view -> {
            // Set EditText visibility here
        });
    }

    private void setUpTimerButtons() {
        Button startBtn = findViewById(R.id.timer_start_button);
        startBtn.setOnClickListener(view -> {
            startTimer(timerDurationInMillis);
            setComponentVisibility(true);
        });

        Button pauseBtn = findViewById(R.id.timer_pause_button);
        pauseBtn.setOnClickListener(view -> {
            if (timer != null){
                if (isTimerPaused){
                    resumeTimer();
                    pauseBtn.setText("Pause");
                }
                else{
                    pauseTimer();
                    pauseBtn.setText("Resume");
                }
            }
        });

        Button resetBtn = findViewById(R.id.timer_reset_button);
        resetBtn.setOnClickListener(view -> resetTimer(startBtn, pauseBtn));
    }

    private void startTimer(long durationInMillis){
        timer = new CountDownTimer(durationInMillis, NUM_MILLIS_IN_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Remember the time left after each tick
                timeLeftInMillis = millisUntilFinished;
                // Update timer textview
                long minutes = millisUntilFinished / (NUM_MILLIS_IN_SECOND * NUM_SECONDS_IN_MINUTE);
                long seconds = (millisUntilFinished / NUM_MILLIS_IN_SECOND) - minutes * NUM_SECONDS_IN_MINUTE;
                timerText.setText(getString(R.string.timer_textview, minutes, seconds));
            }

            @Override
            public void onFinish() {
                // TODO: Handle timer finish notification and sound here
            }
        };
        timer.start();
        isTimerPaused = false;
    }

    private void pauseTimer(){
        timer.cancel();
        isTimerPaused = true;
    }

    private void resumeTimer(){
        // Resume timer using timeLeftInMillis
        startTimer(timeLeftInMillis);
        isTimerPaused = false;
    }

    // TODO Clean this function up
    private void resetTimer(Button start, Button pause){
        // Reset timer
        timer.cancel();
        timerText.setText(getString(R.string.timer_textview, 0, 0));
        isTimerPaused = false;

        // Reset button text and visibility
        setComponentVisibility(false);
        pause.setText("Pause");
    }

    private long convertMinutesToMillis(int numMinutes){
        return numMinutes * (NUM_MILLIS_IN_SECOND * NUM_SECONDS_IN_MINUTE);
    }

    private void setComponentVisibility(boolean isTimerRunning) {
        // Timer is currently running, display pause and reset buttons
        if (isTimerRunning){
            findViewById(R.id.timer_duration_radiogroup).setVisibility(View.INVISIBLE);
            findViewById(R.id.timer_start_button).setVisibility(View.INVISIBLE);
            findViewById(R.id.timer_button_group).setVisibility(View.VISIBLE);
        }
        // Timer is not running, display duration inputs and start button
        else{
            findViewById(R.id.timer_duration_radiogroup).setVisibility(View.VISIBLE);
            findViewById(R.id.timer_start_button).setVisibility(View.VISIBLE);
            findViewById(R.id.timer_button_group).setVisibility(View.INVISIBLE);
        }
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, TimerActivity.class);
    }
}