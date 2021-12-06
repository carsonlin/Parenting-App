package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import Model.TakeBreath;

//activity for Take Breath
public class TakeBreathActivity extends AppCompatActivity {
    private int numberOfBreaths;
    private boolean threeSecondsPassed = false;
    private boolean tenSecondsPassed = false;
    private final long ONE_SECOND_IN_MILLISECONDS = 1000;
    private final long TEN_SECONDS_IN_MILLISECONDS = 10000;
    private final long SEVEN_SECONDS = 7;
    private TakeBreath takeBreath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);
        setUpToolBar();
        setUpBreathCounter();
        updateButtonFunctionality();
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

    private void setUpBreathCounter(){
        Button navigateToBreathButton = findViewById(R.id.startButton);
        EditText breathCountEditText = findViewById(R.id.editTextNumberBreaths);
        TextView numOfBreathsTextView = findViewById(R.id.numberOfBreathsValue);
        TextView NbreathsTextView = findViewById(R.id.breathsMessage);
        ImageView minusimg = findViewById(R.id.minusImage);
        ImageView plusimg = findViewById(R.id.plusImage);
        numberOfBreaths = 1;
        NbreathsTextView.setText(String.format(getString(R.string.breathsMessage),numberOfBreaths));
        minusimg.setOnClickListener(view -> {
            int minus = Integer.parseInt(breathCountEditText.getText().toString()) - 1;
            numberOfBreaths = minus;
            if (minus < 1){
                breathCountEditText.setText("1");
            }
            else{
                breathCountEditText.setText(String.valueOf(minus));
                NbreathsTextView.setText(String.format(getString(R.string.breathsMessage),minus));
            }
        });

        plusimg.setOnClickListener(view -> {
            int addition = Integer.parseInt(breathCountEditText.getText().toString()) + 1;
            numberOfBreaths = addition;

            if (addition > 10){
                breathCountEditText.setText("10");
            }
            else{
                breathCountEditText.setText(String.valueOf(addition));
                NbreathsTextView.setText(String.format(getString(R.string.breathsMessage),addition));
            }
        });

    }

    //Used to change button functionality depending on state
    @SuppressLint("ClickableViewAccessibility")
    private void updateButtonFunctionality(){
        takeBreath = new TakeBreath(TakeBreathActivity.this);
        Button button = findViewById(R.id.inhaleExhaleButton);
        ImageView animate1 = findViewById(R.id.imgAnimation1);
        ImageView animate2 = findViewById(R.id.imgAnimation2);
        button.setOnClickListener(null);
        button.setOnTouchListener(null);
        takeBreath.setTakeBreathButton(button);
        takeBreath.setAnimation1(animate1);
        takeBreath.setAnimation2(animate2);
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
        Button startbutton = findViewById(R.id.startButton);
        TextView textviewBreathsMessage = findViewById(R.id.breathsMessage);
        textviewBreathsMessage.setVisibility(View.VISIBLE);
        TextView textViewHelpMessage = findViewById(R.id.helpMessage);
        textViewHelpMessage.setVisibility(View.GONE);
        startbutton.setOnClickListener(view -> {
            if(numberOfBreaths <= 0){
                Toast.makeText(this,"Choose a valid number of breaths", Toast.LENGTH_SHORT).show();
                return;
            }
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
        Button startButton = findViewById(R.id.startButton);
        startButton.setVisibility(View.GONE);
        View pickBreaths = findViewById(R.id.dropDownBreaths);
        pickBreaths.setVisibility(View.GONE);
        textViewHelpText.setVisibility(View.VISIBLE);
        textViewHelpText.setText(R.string.breath_in_message);
        CountDownTimer timer = inhaleTimer();
        TextView numberOfBreathsTextView = findViewById(R.id.numberOfBreathsValue);
        numberOfBreathsTextView.setText(String.valueOf(numberOfBreaths));
        button.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    takeBreath.setInhale();
                    timer.start();
                    break;
                case MotionEvent.ACTION_UP:
                    timer.cancel();
                    if(threeSecondsPassed){
                        takeBreath.stopAllAnimation();
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
        textViewHelpText.setText(R.string.breath_in_ten_second_message);
        TextView numberOfBreathsTextView = findViewById(R.id.numberOfBreathsValue);
        CountDownTimer timer = exhaleTimer();
        takeBreath.setExhale();
        takeBreath.setBreathingOut(true);
        timer.start();
    }

    private void completeBreath() throws IOException {
        TextView numberOfBreathsTextView = findViewById(R.id.numberOfBreathsValue);
        Button button = findViewById(R.id.inhaleExhaleButton);
        TextView textViewHelpText = findViewById(R.id.help_message_text);
        if (threeSecondsPassed) {
            numberOfBreaths--;
            if (numberOfBreaths != 0) {
                threeSecondsPassed = false;
                tenSecondsPassed = false;
                numberOfBreathsTextView.setText(String.valueOf(numberOfBreaths));
                button.setText(R.string.inhaleButtonText);
                updateButtonFunctionality();
            }
            else {
                takeBreath.stopAllAnimation();
                threeSecondsPassed = false;
                tenSecondsPassed = false;
                button.setText(R.string.goodJobButtonText);
                takeBreath.retractCircle();
                numberOfBreathsTextView.setText(String.valueOf(numberOfBreaths));
                textViewHelpText.setText(R.string.good_job_help_text);
                setUpBeginButton();
                updateButtonFunctionality();
                takeBreath.suspendAnimation();
            }
        }
    }

    private CountDownTimer exhaleTimer(){
        return new CountDownTimer(TEN_SECONDS_IN_MILLISECONDS, ONE_SECOND_IN_MILLISECONDS) {
            @Override
            public void onTick(long millisUntilFinished) {
                long durationInSeconds = millisUntilFinished/ONE_SECOND_IN_MILLISECONDS;
                if(durationInSeconds == SEVEN_SECONDS){
                    threeSecondsPassed = true;
                    if (takeBreath.getBreathingOut()) {
                        try {
                            completeBreath();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onFinish() {
                tenSecondsPassed = true;
                if (takeBreath.getBreathingOut())
                    takeBreath.suspendAnimation();

            }
        };
    }

    private void setUpGoodJobButton(){
        Button button = findViewById(R.id.inhaleExhaleButton);
        takeBreath.suspendAnimation();
        button.setOnClickListener(view -> {
            Button button2 = findViewById(R.id.inhaleExhaleButton);
            Button startbutton = findViewById(R.id.startButton);
            EditText breathCountEditText = findViewById(R.id.editTextNumberBreaths);
            TextView textviewBreathsMessage = findViewById(R.id.breathsMessage);
            TextView textViewHelpMessage = findViewById(R.id.helpMessage);
            TextView NbreathsTextView = findViewById(R.id.breathsMessage);
            TextView textViewHelpText = findViewById(R.id.help_message_text);
            View pickBreaths = findViewById(R.id.dropDownBreaths);
            pickBreaths.setVisibility(View.VISIBLE);
            textviewBreathsMessage.setVisibility(View.GONE);
            textViewHelpMessage.setVisibility(View.GONE);
            startbutton.setVisibility(View.VISIBLE);
            startbutton.setVisibility(View.VISIBLE);
            textViewHelpText.setVisibility(View.GONE);
            numberOfBreaths = 1;
            breathCountEditText.setText("1");
            NbreathsTextView.setText(String.format(getString(R.string.breathsMessage),numberOfBreaths));
            button2.setText(R.string.beginButtonText);
            updateButtonFunctionality();
        });
    }
}