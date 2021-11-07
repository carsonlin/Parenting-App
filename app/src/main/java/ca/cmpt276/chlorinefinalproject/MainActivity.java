package ca.cmpt276.chlorinefinalproject;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Model.Coin;

public class MainActivity extends AppCompatActivity {

    ImageView cardFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardFace = findViewById(R.id.main_activity_card_face);

        Coin coin = new Coin(MainActivity.this, cardFace);

        cardFace.setOnClickListener(view -> System.out.println("head? " + coin.flip().isHead()));
    }
}