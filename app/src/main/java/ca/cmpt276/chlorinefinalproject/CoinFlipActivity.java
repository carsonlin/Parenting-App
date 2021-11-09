package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import Model.ChildPick;
import Model.Coin;
import Model.Game;
import Model.GameManager;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipBinding;

public class CoinFlipActivity extends AppCompatActivity {

    private ActivityCoinFlipBinding binding;
    private GameManager gameManager;
    private Coin coin;
    private TextView editTextChildPick;
    private boolean isHead;
    private String child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        binding = ActivityCoinFlipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView cardFace = findViewById(R.id.main_activity_card_face);
        editTextChildPick = findViewById(R.id.coin_flip_text_view);

        extractIntentExtras();

        setUpActionBar();
        coin = new Coin(CoinFlipActivity.this, cardFace);
        gameManager = new GameManager(CoinFlipActivity.this);
        cardFace.setOnClickListener(view -> coin.flip());
    }

    private void extractIntentExtras() {
        Intent intent = getIntent();
        child = intent.getStringExtra("child");
        isHead = intent.getStringExtra("bet").equals("head");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
            backButtonpressedBehaviour();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        System.out.println("pressed");
        backButtonpressedBehaviour();
    }

    private void backButtonpressedBehaviour(){
            if(!coin.isAnimating() && coin.isInteracted()) {
                ChildPick childPickInstance = new ChildPick(child, isHead);
                Game game = new Game(childPickInstance);
                game.setHead(coin.isHead());
                gameManager.addGame(game);
                gameManager.saveGameToSharedPreference();
            }
            ArrayList<String> children = (ArrayList<String>) EditChildActivity.getChildrenSharedPreferences(CoinFlipActivity.this);

            goToMainActivity(children.isEmpty());

            finish();
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        if (this.isHead){
            editTextChildPick.setText(String.format((getString(R.string.coin_flip_text_view)), this.child, "Heads"));
        }
        else{
            editTextChildPick.setText(String.format((getString(R.string.coin_flip_text_view)), this.child, "Tails"));
        }
    }

    public void goToMainActivity(boolean childEmpty){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        if(!childEmpty)
            intent = new Intent(getApplicationContext(),CoinFlipChooseActivity.class);

        startActivity(intent);
    }
}
