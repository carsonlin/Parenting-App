package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ca.cmpt276.chlorinefinalproject.model.coin;

public class MainActivity extends AppCompatActivity {

    ImageView cardFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardFace = findViewById(R.id.main_activity_card_face);

        coin coin = new coin(MainActivity.this,cardFace);

        cardFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("head? "+coin.flip().isHead());

            }
        });



    }
}