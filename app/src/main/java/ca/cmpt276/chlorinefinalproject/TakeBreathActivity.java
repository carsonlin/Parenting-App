package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//activity for Take Breath
public class TakeBreathActivity extends AppCompatActivity {
    private int numberOfBreaths;
    private boolean threeSecondsPassed = false;
    private boolean tenSecondsPassed = false;
    private final long ONE_SECOND_IN_MILLISECONDS = 1000;
    private final long TEN_SECONDS_IN_MILLISECONDS = 10000;
    private final long SEVEN_SECONDS = 7;
    private static final String PREFERENCES = "appPrefs";
    private static final String NUMBER_OF_BREATHS_KEY = "numOfBreaths";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        setUpToolBar();
        setUpSpinner();
        updateButtonFunctionality();
        numberOfBreaths = getNumberOfBreathsSharedPref(this);
        TextView numOfBreathsTextView = findViewById(R.id.numberOfBreathsValue);
        numOfBreathsTextView.setText(String.valueOf(numberOfBreaths));
    }

    private void setUpToolBar(){
        ca.cmpt276.chlorinefinalproject.databinding.ActivityTakeBreathBinding binding = ca.cmpt276.chlorinefinalproject.databinding.ActivityTakeBreathBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Take Breath");
    }

    //dropdown menu to choose from 1-10 breaths
    private void setUpSpinner(){
        Spinner dropdown = findViewById(R.id.dropDownBreaths);
        TextView numOfBreathsTextView = findViewById(R.id.numberOfBreathsValue);
        String[] items = new String[]{"Choose Number Of Breaths","1", "2", "3","4","5","6","7","8","9","10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(position == 0){
                    return;
                }
                String breathsChoice = items[position];
                numberOfBreaths = Integer.parseInt(breathsChoice);
                updateNumberOfBreathsSharedPref(TakeBreathActivity.this);
                numOfBreathsTextView.setText(breathsChoice);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //Used to change button functionality depending on state
    @SuppressLint("ClickableViewAccessibility")
    private void updateButtonFunctionality(){
        Button button = findViewById(R.id.inhaleExhaleButton);
        button.setOnClickListener(null);
        button.setOnTouchListener(null);
        String string = button.getText().toString();
        switch(string){
            case "Begin":
                setUpBeginButton();
                break;
            case "In":
                setUpInhaleButton();
                break;
            case "Out":
                setUpExhaleButton();
                break;
            case "Good Job":
                setUpGoodJobButton();
        }
    }

    private void setUpBeginButton(){
        Button button = findViewById(R.id.inhaleExhaleButton);
        TextView textviewBreathsMessage = findViewById(R.id.breathsMessage);
        textviewBreathsMessage.setVisibility(View.VISIBLE);
        TextView textViewHelpMessage = findViewById(R.id.helpMessage);
        Spinner dropdown = findViewById(R.id.dropDownBreaths);
        dropdown.setSelection(0);
        numberOfBreaths = getNumberOfBreathsSharedPref(this);
        TextView numOfBreathsTextView = findViewById(R.id.numberOfBreathsValue);
        numOfBreathsTextView.setText(String.valueOf(numberOfBreaths));
        dropdown.setVisibility(View.VISIBLE);
        textViewHelpMessage.setVisibility(View.GONE);
        button.setOnClickListener(view -> {
            if(numberOfBreaths <= 0){
                Toast.makeText(this,"Choose a valid number of breaths", Toast.LENGTH_SHORT).show();
                return;
            }
            dropdown.setVisibility(View.GONE);
            button.setText(R.string.inhaleButtonText);
            textviewBreathsMessage.setVisibility(View.GONE);
            updateButtonFunctionality();
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpInhaleButton(){
        Button button = findViewById(R.id.inhaleExhaleButton);
        TextView textViewHelpMessage = findViewById(R.id.helpMessage);
        textViewHelpMessage.setVisibility(View.VISIBLE);
        TextView textViewHelpText = findViewById(R.id.help_message_text);
        textViewHelpText.setText(R.string.breath_in_message);
        CountDownTimer timer = inhaleTimer();
        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    timer.start();
                    break;
                case MotionEvent.ACTION_UP:
                    timer.cancel();
                    if(threeSecondsPassed){
                        button.setText(R.string.exhaleButtonText);
                        updateButtonFunctionality();
                        threeSecondsPassed = false;
                        tenSecondsPassed = false;
                    }
                    break;
            }
            return true;
        });
    }

    private CountDownTimer inhaleTimer(){
        return new CountDownTimer(TEN_SECONDS_IN_MILLISECONDS, ONE_SECOND_IN_MILLISECONDS) {
            @Override
            public void onTick(long millisUntilFinished) {
                long durationInSeconds = millisUntilFinished/ONE_SECOND_IN_MILLISECONDS;
                if(durationInSeconds == SEVEN_SECONDS){
                    threeSecondsPassed = true;
                }
            }
            @Override
            public void onFinish() {
                TextView textView = findViewById(R.id.help_message_text);
                textView.setText(R.string.breath_in_ten_second_message);
                tenSecondsPassed = true;
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpExhaleButton() {
        Button button = findViewById(R.id.inhaleExhaleButton);
        button.setText(R.string.exhaleButtonText);
        TextView textViewHelpText = findViewById(R.id.help_message_text);
        textViewHelpText.setText(R.string.breath_out_message);
        TextView numberOfBreathsTextView = findViewById(R.id.numberOfBreathsValue);
        CountDownTimer timer = exhaleTimer();
        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    timer.start();
                    break;
                case MotionEvent.ACTION_UP:
                    timer.cancel();
                    if(threeSecondsPassed){
                        numberOfBreaths--;
                        updateNumberOfBreathsSharedPref(this);
                        if(numberOfBreaths != 0){
                            threeSecondsPassed = false;
                            tenSecondsPassed = false;
                            numberOfBreathsTextView.setText(String.valueOf(numberOfBreaths));
                            button.setText(R.string.inhaleButtonText);
                            updateButtonFunctionality();
                        }
                        else{
                            threeSecondsPassed = false;
                            tenSecondsPassed = false;
                            button.setText(R.string.goodJobButtonText);
                            numberOfBreathsTextView.setText(String.valueOf(numberOfBreaths));
                            textViewHelpText.setText(R.string.good_job_help_text);
                            updateButtonFunctionality();
                        }
                    }
                    break;
            }
            return true;
        });
    }

    private CountDownTimer exhaleTimer(){
        return new CountDownTimer(TEN_SECONDS_IN_MILLISECONDS, ONE_SECOND_IN_MILLISECONDS) {
            @Override
            public void onTick(long millisUntilFinished) {
                long durationInSeconds = millisUntilFinished/ONE_SECOND_IN_MILLISECONDS;
                if(durationInSeconds == SEVEN_SECONDS){
                    threeSecondsPassed = true;
                }
            }
            @Override
            public void onFinish() {
                tenSecondsPassed = true;
            }
        };
    }

    private void setUpGoodJobButton(){
        Button button = findViewById(R.id.inhaleExhaleButton);
        button.setOnClickListener(view -> {
            button.setText(R.string.beginButtonText);
            updateButtonFunctionality();
        });
    }

    private void updateNumberOfBreathsSharedPref(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(NUMBER_OF_BREATHS_KEY).apply();
        editor.putInt(NUMBER_OF_BREATHS_KEY, numberOfBreaths).apply();
    }

    private int getNumberOfBreathsSharedPref(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        int temp = 0;
        return prefs.getInt(NUMBER_OF_BREATHS_KEY, temp);
    }
}