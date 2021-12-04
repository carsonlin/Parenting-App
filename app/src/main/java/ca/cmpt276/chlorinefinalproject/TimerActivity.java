package ca.cmpt276.chlorinefinalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// Activity that runs timer of preset or custom duration in minutes
public class TimerActivity extends AppCompatActivity {
    public static final int NUM_MILLIS_IN_SECOND = 1000;
    public static final int NUM_SECONDS_IN_MINUTE = 60;
    public static final String REMAINING_TIME = "remainingTime";
    public static final String TIMER_RATE = "timerRate";
    public static final String INTENT_FILTER = "time";
    public static final String ORIGINAL_TIME = "originalTime";
    public static final String PAUSE_TIME = "pauseTime";
    public static final String TIMER_PREFS = "timerSharedPref";
    private TextView timerText;
    private TextView timerRateText;
    private boolean isTimerPaused = false;
    private long timerDurationInMillis;
    private long timeLeftInMillis;
    private double timerSpeedRate = 1.00;

    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        sharedPref = this.getSharedPreferences(TIMER_PREFS, MODE_PRIVATE);
        timeLeftInMillis = sharedPref.getLong(PAUSE_TIME, MODE_PRIVATE);


        View layout = findViewById(R.id.timer_layout);
        layout.setBackgroundResource(R.drawable.sleeping_dog);

        setComponentVisibility(true);

        if (!TimerService.isRunning()){
            if (timeLeftInMillis != 0){
                isTimerPaused = true;
                Button pauseBtn = findViewById(R.id.timer_pause_button);
                pauseBtn.setText(R.string.timer_resume_button_text);
            }
            else{
                layout.setBackgroundResource(0);
                setComponentVisibility(false);
            }
        }

        timerText = findViewById(R.id.timer_text_view);
        timerText.setText(getString(R.string.timer_textview, 0, 0));
        timerRateText = findViewById(R.id.timer_rate_text);
        timerRateText.setText(getString(R.string.timer_rate_textview, timerSpeedRate * 100));

        setupToolbar();

        timerText.setText(getString(R.string.timer_textview, getMinutesFromMillis(timeLeftInMillis), getSecondsFromMillis(timeLeftInMillis)));
        setUpTimerButtons();
        setUpInputRadioButtons();
        setUpCustomInput();

        IntentFilter filter = new IntentFilter(INTENT_FILTER);
        this.registerReceiver(br, filter);
    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            timeLeftInMillis = intent.getLongExtra(REMAINING_TIME, 0);
            long minutes = getMinutesFromMillis(timeLeftInMillis);
            long seconds = getSecondsFromMillis(timeLeftInMillis);
            timerText.setText(getString(R.string.timer_textview, minutes, seconds));
        }
    };

    public void startTimerService(long timeInMs, double timerSpeedRate){
        Intent serviceIntent = new Intent(this, TimerService.class);
        serviceIntent.putExtra(REMAINING_TIME, timeInMs);
        serviceIntent.putExtra(TIMER_RATE, timerSpeedRate);
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

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_timeractivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        else if(id == R.id.timer_rate){
            PopupMenu popup = new PopupMenu(this, findViewById(R.id.timer_rate));
            int[] timerRates = getResources().getIntArray(R.array.timer_rates);

            // Populate menu items
            for (int rate : timerRates){
                popup.getMenu().add(rate + "%");
            }

            popup.setOnMenuItemClickListener(menuItem -> {
                // Grab numerical value from menu title
                String str = menuItem.getTitle().toString();
                int percent = Integer.parseInt(str.substring(0, str.length() - 1));
                timerSpeedRate = (double) percent / 100;
                timerRateText.setText(getString(R.string.timer_rate_textview, percent));

                // restart timer service with new rate
                if (TimerService.isRunning()){
                    stopTimerService();
                    startTimerService(timeLeftInMillis, timerSpeedRate);
                }
                return false;
            });
            popup.show();
        }
        return super.onOptionsItemSelected(item);
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
                    timerDurationInMillis = getMillisFromMinutes(customDuration);
                    timerText.setText(getString(R.string.timer_textview, customDuration, 0));
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

            btn.setOnClickListener(view -> {
                timerDurationInMillis = getMillisFromMinutes(minutes);
                timerText.setText(getString(R.string.timer_textview, minutes, 0));
            });
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
            }
            else{
                editText.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setUpTimerButtons() {
        View layout = findViewById(R.id.timer_layout);

        Button startBtn = findViewById(R.id.timer_start_button);
        startBtn.setOnClickListener(view -> {
            if (timerDurationInMillis > 0){
                setComponentVisibility(true);
                startTimerService(timerDurationInMillis, timerSpeedRate);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong(ORIGINAL_TIME, timerDurationInMillis);
                editor.apply();

                layout.setBackgroundResource(R.drawable.sleeping_dog);
            }
            else{
                Toast.makeText(TimerActivity.this, R.string.invalid_timer_duration, Toast.LENGTH_SHORT).show();
            }
        });

        Button pauseBtn = findViewById(R.id.timer_pause_button);
        pauseBtn.setOnClickListener(view -> {
            if (timeLeftInMillis != 0) {
                if (isTimerPaused) {
                    layout.setBackgroundResource(R.drawable.sleeping_dog);
                    startTimerService(timeLeftInMillis, timerSpeedRate);
                    pauseBtn.setText(R.string.timer_pause_button_text);
                }
                else {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong(PAUSE_TIME, timeLeftInMillis);
                    editor.apply();

                    stopTimerService();
                    pauseBtn.setText(R.string.timer_resume_button_text);
                }
            }
        });

        Button resetBtn = findViewById(R.id.timer_reset_button);
        resetBtn.setOnClickListener(view -> {
            resetTimer();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(PAUSE_TIME, 0);
            editor.apply();

            timerDurationInMillis = sharedPref.getLong(ORIGINAL_TIME, 0);

            long minutes = getMinutesFromMillis(timerDurationInMillis);
            long seconds = getSecondsFromMillis(timerDurationInMillis);
            timerText.setText(getString(R.string.timer_textview, minutes, seconds));

            layout.setBackgroundResource(0);

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

    private long getMillisFromMinutes(int numMinutes){
        return numMinutes * (NUM_MILLIS_IN_SECOND * NUM_SECONDS_IN_MINUTE);
    }

    private int getMinutesFromMillis(long ms){
        return (int) ms / (NUM_MILLIS_IN_SECOND * NUM_SECONDS_IN_MINUTE);
    }

    private int getSecondsFromMillis(long ms){
        return (int) (ms / NUM_MILLIS_IN_SECOND) - (getMinutesFromMillis(ms) * NUM_SECONDS_IN_MINUTE);
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