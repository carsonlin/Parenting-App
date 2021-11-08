package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import Model.Coin;
import Model.Game;
import Model.GameManager;
import Model.ChildPick;
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
        editTextChildPick = findViewById(R.id.editTextChildPick);
        TextView editTextGameCount = findViewById(R.id.textViewgameHistory);
        TextView editTextGameHistory = findViewById(R.id.editTextHistory);

        Intent intent = getIntent();
        child = intent.getStringExtra("child");
        isHead = intent.getStringExtra("bet").equals("head");

        setUpActionBar();

        coin = new Coin(CoinFlipActivity.this, cardFace);
        gameManager = new GameManager(CoinFlipActivity.this);
        cardFace.setOnClickListener(view -> coin.flip());
        editTextGameCount.setText(gameManager.getSavedGamesFromSharedPreferences().size() + " plays");

        editTextGameHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){

            if(!coin.isAnimating() && coin.isInteracted()) {
                ChildPick childPickInstance = new ChildPick(child, isHead);
                Game game = new Game(childPickInstance);
                game.setHead(coin.isHead());
                gameManager.addGame(game);
                gameManager.saveGameToSharedPreference();
            }
            if (child.isEmpty()){
                goToMainActivity();
            }

            finish();
            return true;
        }
        return false;
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        String headText = this.child + " flip coin";
        editTextChildPick.setText(this.child + " picked" + (this.isHead ?" Heads":" Tails"));

        if (this.child.isEmpty()){
            headText = " flip coin ";
            editTextChildPick.setText("no child configured");
        }
        ab.setTitle(headText);
    }

    public void goToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
