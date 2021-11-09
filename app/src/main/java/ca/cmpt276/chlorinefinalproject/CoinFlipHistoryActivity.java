package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Model.GameManager;
import Model.RecyclerViewCoinFlipHistory;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipHistoryBinding;

// Activity to display history of past coin flips
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

        setAdapters();
        setUpActionBar();
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

    private void backButtonPressedBehaviour() {
        Intent intent = new Intent(getApplicationContext(), CoinFlipChooseActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void setAdapters(){
        RecyclerViewCoinFlipHistory adapter = new RecyclerViewCoinFlipHistory(gameManager, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}