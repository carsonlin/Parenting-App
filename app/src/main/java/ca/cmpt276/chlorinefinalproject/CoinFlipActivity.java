package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import Model.Coin;
import Model.Game;
import Model.GameManager;
import Model.childPick;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipBinding;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityEditOrDeleteChildBinding;

public class CoinFlipActivity extends AppCompatActivity {

    private ActivityCoinFlipBinding binding;
    private GameManager gameManager;
    private Coin coin;
    private TextView editTextChildPick;
    private TextView editTextGamecount;
    private TextView editTextgameHistory;
    private ImageView cardFace;
    private boolean head;
    private String child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        binding = ActivityCoinFlipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cardFace = (ImageView) findViewById(R.id.main_activity_card_face);
        editTextChildPick = (TextView) findViewById(R.id.editTextChildPick);
        editTextGamecount = (TextView) findViewById(R.id.textViewgameHistory);
        editTextgameHistory = (TextView) findViewById(R.id.editTextHistory);
        Intent intent = getIntent();
        child = intent.getStringExtra("child");
        head = intent.getStringExtra("bet").equals("head")==true?true:false;
        setUpActionBar();
        coin = new Coin(CoinFlipActivity.this, cardFace);
        gameManager = new GameManager(CoinFlipActivity.this,coin);
        cardFace.setOnClickListener(view -> coin.flip());
        editTextGamecount.setText(gameManager.savedGames().size()+" plays");

        editTextgameHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if (item.getItemId() == android.R.id.home){
            if(!coin.isAnimating()) {
                childPick childPickinstance = new childPick(child,head);
                Game game = new Game(childPickinstance,coin);
                game.setHead(coin.isHead());
                gameManager.addGame(game);
                gameManager.saveGame();
            }
            if (child.isEmpty())
                goTomainactivity();

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
        String headtext = this.child + " flip coin";
        editTextChildPick.setText(this.child+" picked"+(this.head==true?" Heads":" Tails"));

        if (this.child.isEmpty()){
            headtext = " flip coin ";
            editTextChildPick.setText("no child configured");
        }

        ab.setTitle(headtext);

    }

    public void goTomainactivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }



}
