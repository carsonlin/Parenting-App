package ca.cmpt276.chlorinefinalproject;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Model.Coin;

public class CoinFlipActivity extends AppCompatActivity {

    ImageView cardFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        cardFace = findViewById(R.id.main_activity_card_face);

        Coin coin = new Coin(CoinFlipActivity.this, cardFace);

        cardFace.setOnClickListener(view -> System.out.println("head? " + coin.flip().isHead()));
    }
}
