package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {
    public static final int NUM_MILLIS_IN_SECOND = 1000;
    public static final int NUM_SECONDS_IN_MINUTE = 60;
    public static final String REMAINING_TIME = "remainingTime";
    public static final String INTENT_FILTER = "time";
    private TextView timerText;
    private boolean isTimerPaused = false;
    private long timerDurationInMillis;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerText = findViewById(R.id.timer_text_view);
        timerText.setText(getString(R.string.timer_textview, 0, 0));

        setUpTimerButtons();
        setUpInputRadioButtons();
        setUpCustomInput();
        setComponentVisibility(TimerService.isRunning());

        IntentFilter filter = new IntentFilter(INTENT_FILTER);
        this.registerReceiver(br, filter);

    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            timeLeftInMillis = intent.getLongExtra(REMAINING_TIME, 0);
            long minutes = timeLeftInMillis / (NUM_MILLIS_IN_SECOND * NUM_SECONDS_IN_MINUTE);
            long seconds = (timeLeftInMillis / NUM_MILLIS_IN_SECOND) - minutes * NUM_SECONDS_IN_MINUTE;
            timerText.setText(getString(R.string.timer_textview, minutes, seconds));
        }
    };

    public void startTimerService(long timeInMs){
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(REMAINING_TIME, timeInMs);
        startService(serviceIntent);
        isTimerPaused = false;
    }

    public void stopTimerService(){
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
        isTimerPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(INTENT_FILTER);
        this.registerReceiver(br, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(br);
        }
        catch (IllegalArgumentException e){
            // already unregistered
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(br);
        }
        catch (IllegalArgumentException e){
            // already unregistered
        }
    }

    private void setUpCustomInput() {
        EditText editText = findViewById(R.id.timer_custom_input);

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
            btn.setOnClickListener(view -> timerDurationInMillis = convertMinutesToMillis(minutes));
            group.addView(btn);
        }

        // Create custom input radio button
        RadioButton customBtn = new RadioButton(this);
        customBtn.setText(R.string.timer_custom_radio_text);
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
            setComponentVisibility(true);
            startTimerService(timerDurationInMillis);
        });

        Button pauseBtn = findViewById(R.id.timer_pause_button);
        pauseBtn.setOnClickListener(view -> {
            if (timeLeftInMillis != 0) {
                if (isTimerPaused) {
                    startTimerService(timeLeftInMillis);
                    pauseBtn.setText(R.string.timer_pause_button_text);
                } else {
                    stopTimerService();
                    pauseBtn.setText(R.string.timer_resume_button_text);
                }
            }
        });

        Button resetBtn = findViewById(R.id.timer_reset_button);
        resetBtn.setOnClickListener(view -> {
            resetTimer();
            stopTimerService();
        });
    }

    private void resetTimer(){
        stopTimerService();
        timerText.setText(getString(R.string.timer_textview, 0, 0));
        isTimerPaused = false;
        setComponentVisibility(false);
        Button pauseButton = findViewById(R.id.timer_pause_button);
        pauseButton.setText(R.string.timer_pause_button_text);
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