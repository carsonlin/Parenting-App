package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    private CountDownTimer timer;
    private TextView timerText;
    private boolean isTimerPaused;
    private long timerTotalDurationInMillis;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerText = findViewById(R.id.timer_text_view);
        setUpTimerButtons();

        // TODO : Handle custom duration inputs
        timerTotalDurationInMillis = convertMinutesToMillis(1);
    }

    private void setUpTimerButtons() {
        Button startBtn = findViewById(R.id.timer_start_button);
        startBtn.setOnClickListener(view -> {
            startTimer(timerTotalDurationInMillis);
            // Make button invisible -> can only start once
            startBtn.setVisibility(View.INVISIBLE);
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
        timer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Remember the time left
                timeLeftInMillis = millisUntilFinished;
                timerText.setText(String.valueOf(millisUntilFinished / 1000));
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

    private void resetTimer(Button start, Button pause){
        // Reset timer
        timer.cancel();
        timerText.setText("0");
        isTimerPaused = false;

        // Reset button text and visibility
        start.setVisibility(View.VISIBLE);
        pause.setText("Pause");
    }

    private long convertMinutesToMillis(int numMinutes){
        // 60000 milliseconds in a minute
        return numMinutes * 60000L;
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, TimerActivity.class);
    }
}