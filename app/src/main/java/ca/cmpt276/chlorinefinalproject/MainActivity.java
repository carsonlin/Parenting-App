package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import Model.ChildManager;

// Main Ui for the app
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupButton(R.id.taskCompleteButton, CoinFlipChooseActivity.class);
        setupButton(R.id.timerButton, TimerActivity.class);
        setupButton(R.id.childrenConfigButton, ListOfChildrenActivity.class);
        setupButton(R.id.whoseTurnButton, WhoseTurnActivity.class);

        // To initialize child manager with objects from shared preferences
        ChildManager childManager = ChildManager.getInstance();
        childManager.getListOfChildObjects(this);
    }

    public void setupButton(int buttonId, Class<?> classToStart){
        Button button = findViewById(buttonId);

        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, classToStart);
            startActivity(intent);
        });
    }
}