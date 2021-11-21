package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import Model.ChildPick;
import Model.Coin;
import Model.Game;
import Model.GameManager;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipBinding;

// Activity that holds coin flip animation
public class CoinFlipActivity extends AppCompatActivity {
    public static final String CHILD = "child";
    public static final String BET = "bet";
    private ActivityCoinFlipBinding binding;
    private GameManager gameManager;
    private Coin coin;
    private boolean isHead;
    private String child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
        binding = ActivityCoinFlipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        extractIntentExtras();
        setUpTextView();
        setUpActionBar();
        setUpCoin();
    }

    private void setUpCoin() {
        ImageView cardFace = findViewById(R.id.main_activity_card_face);
        coin = new Coin(CoinFlipActivity.this, cardFace);
        gameManager = new GameManager(CoinFlipActivity.this);
        cardFace.setOnClickListener(view -> {
            coin.flip();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Toast toast = Toast.makeText(getApplicationContext(),
                        coin.isHead() ? getString(R.string.text_heads) : getString(R.string.text_tails),
                        Toast.LENGTH_SHORT);
                toast.show();
            }, coin.predictedTime() + 1000);
        });
    }

    private void setUpTextView() {
        TextView editTextChildPick = findViewById(R.id.coin_flip_text_view);
        if (this.child.equals("")){
            editTextChildPick.setText("");
        }
        else if (this.isHead){
            editTextChildPick.setText(String.format((getString(R.string.coin_flip_text_view)), this.child, "Heads"));
        }
        else{
            editTextChildPick.setText(String.format((getString(R.string.coin_flip_text_view)), this.child, "Tails"));
        }
    }

    private void extractIntentExtras() {
        Intent intent = getIntent();
        child = intent.getStringExtra(CHILD);
        isHead = intent.getStringExtra(BET).equals("heads");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
            backButtonPressedBehaviour();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        backButtonPressedBehaviour();
    }

    private void backButtonPressedBehaviour(){
            if(!coin.isAnimating() && coin.isInteracted()) {
                ChildPick childPickInstance = new ChildPick(child, isHead);
                Game game = new Game(childPickInstance);
                game.setHead(coin.isHead());
                gameManager.addGame(game);
                gameManager.saveGameToSharedPreference();
            }
            coin.setAbortAnimation(true);
            ArrayList<String> children = (ArrayList<String>) EditChildActivity.getChildNameSharedPreferences(CoinFlipActivity.this);
            goToMainActivity(children.isEmpty());
            finish();
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void goToMainActivity(boolean childEmpty){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        if(!childEmpty){
            intent = new Intent(getApplicationContext(),CoinFlipChooseActivity.class);
        }
        startActivity(intent);
    }
}
