package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        setupToolbar();

        setUpTimerButtons();
        setUpInputRadioButtons();
        setUpCustomInput();
        setComponentVisibility(false);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){

            //probably have to put code here to preserve the timer when leaving the activity

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpCustomInput() {
        // Text watcher for the custom input edit text
        EditText editText = findViewById(R.id.timer_custom_input);

        // Grab EditText data on change
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try{
                    int customDuration = Integer.parseInt(editText.getText().toString());
                    timerDurationInMillis = convertMinutesToMillis(customDuration);
                }
                catch (NumberFormatException ignored){ }
            }
        });
    }

    private void setUpInputRadioButtons() {
        RadioGroup group = findViewById(R.id.timer_duration_radiogroup);
        int[] durations = getResources().getIntArray(R.array.timer_durations);

        // Create buttons from array of durations
        for (int minutes : durations) {
            RadioButton btn = new RadioButton(this);
            btn.setText(getString(R.string.timer_radio_button_text, minutes));
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            btn.setOnClickListener(view -> timerDurationInMillis = convertMinutesToMillis(minutes));

            group.addView(btn);
        }

        // Create custom input radio button
        RadioButton customBtn = new RadioButton(this);
        customBtn.setText(R.string.timer_custom_radio_text);
        customBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        customBtn.setOnClickListener(view -> {
            EditText editText = findViewById(R.id.timer_custom_input);
            editText.setText("");
        });
        group.addView(customBtn);

        // Set radio group listener for visibility of custom input edit text
        group.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            EditText editText = findViewById(R.id.timer_custom_input);
            if (checkedId == customBtn.getId()){
                editText.setVisibility(View.VISIBLE);
                try{
                    int customDuration = Integer.parseInt(editText.getText().toString());
                    timerDurationInMillis = convertMinutesToMillis(customDuration);
                }
                catch (NumberFormatException ignored){ }
            }
            else{
                editText.setVisibility(View.INVISIBLE);
            }
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
                    pauseBtn.setText(R.string.timer_pause_button_text);
                }
                else{
                    pauseTimer();
                    pauseBtn.setText(R.string.timer_resume_button_text);
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
        pause.setText(R.string.timer_pause_button_text);
    }

    private long convertMinutesToMillis(int numMinutes){
        return numMinutes * (NUM_MILLIS_IN_SECOND * NUM_SECONDS_IN_MINUTE);
    }

    private void setComponentVisibility(boolean isTimerRunning) {
        // Timer is currently running, display pause and reset buttons
        if (isTimerRunning){
            findViewById(R.id.timer_duration_inputs).setVisibility(View.INVISIBLE);
            findViewById(R.id.timer_start_button).setVisibility(View.INVISIBLE);
            findViewById(R.id.timer_button_group).setVisibility(View.VISIBLE);
        }
        // Timer is not running, display duration inputs and start button
        else{
            findViewById(R.id.timer_duration_inputs).setVisibility(View.VISIBLE);
            findViewById(R.id.timer_start_button).setVisibility(View.VISIBLE);
            findViewById(R.id.timer_button_group).setVisibility(View.INVISIBLE);
        }
    }
}