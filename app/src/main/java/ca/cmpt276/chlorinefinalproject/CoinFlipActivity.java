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
import Model.childPick;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipBinding;

public class CoinFlipActivity extends AppCompatActivity {

    private ActivityCoinFlipBinding binding;
    private GameManager gameManager;
    private Coin coin;
    private TextView editTextChildPick;
    private TextView editTextGameCount;
    private TextView editTextGameHistory;
    private ImageView cardFace;
    private boolean head;
    private String child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        binding = ActivityCoinFlipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cardFace = findViewById(R.id.main_activity_card_face);
        editTextChildPick = findViewById(R.id.editTextChildPick);
        editTextGameCount = findViewById(R.id.textViewgameHistory);
        editTextGameHistory = findViewById(R.id.editTextHistory);

        Intent intent = getIntent();
        child = intent.getStringExtra("child");
        head = intent.getStringExtra("bet").equals("head")==true?true:false;

        setUpActionBar();

        coin = new Coin(CoinFlipActivity.this, cardFace);
        gameManager = new GameManager(CoinFlipActivity.this,coin);
        cardFace.setOnClickListener(view -> coin.flip());
        editTextGameCount.setText(gameManager.savedGames().size()+" plays");

        editTextGameHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
            if(!coin.isAnimating()) {
                childPick childPickInstance = new childPick(child,head);
                Game game = new Game(childPickInstance,coin);
                game.setHead(coin.isHead());
                gameManager.addGame(game);
                gameManager.saveGame();
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
        editTextChildPick.setText(this.child + " picked" + (this.head==true?" Heads":" Tails"));

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
