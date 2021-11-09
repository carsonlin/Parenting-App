package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

import Model.Coin;
import Model.GameManager;
import Model.RecyclerViewChildrenPick;
import Model.RecyclerViewCoinFlipHistory;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipChooseBinding;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipHistoryBinding;

public class CoinFlipHistoryActivity extends AppCompatActivity {

    private ActivityCoinFlipHistoryBinding binding;
    private RecyclerView recyclerView;
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_history);

        binding = ActivityCoinFlipHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameManager = new GameManager(CoinFlipHistoryActivity.this);

        recyclerView = findViewById(R.id.listOfgames);

        System.out.println("gammes count "+gameManager.getSavedGamesFromSharedPreferences().size());

        setAdapters();
        setUpActionBar();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(CoinFlipHistoryActivity.this,CoinFlipActivity.class);
            intent.putExtra("child","");
            intent.putExtra("bet","head");
            startActivity(intent);
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
        ab.setTitle(" Game history ");
    }

    public void setAdapters(){


        RecyclerViewCoinFlipHistory adapter = new RecyclerViewCoinFlipHistory(gameManager,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }


}