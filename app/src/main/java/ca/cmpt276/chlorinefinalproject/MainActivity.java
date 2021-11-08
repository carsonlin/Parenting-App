package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setupButton(R.id.coinFlipButton, {name of coin flipping class}.class);
        setupButton(R.id.timerButton, TimerActivity.class);
        setupButton(R.id.childrenConfigButton, ListOfChildren.class);
    }

    public void setupButton(int buttonId, Class<?> classToStart){
        Button button = findViewById(buttonId);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, classToStart);
            startActivity(intent);
        });
    }
}