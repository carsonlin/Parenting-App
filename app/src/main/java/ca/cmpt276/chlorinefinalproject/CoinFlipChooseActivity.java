package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Model.GameManager;
import Model.RecyclerViewChildrenPick;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipChooseBinding;

// Activity to select a child for a coin flip
public class CoinFlipChooseActivity extends AppCompatActivity {
    private ActivityCoinFlipChooseBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<String> listOfChildren;
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_choose);
        binding = ActivityCoinFlipChooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameManager = new GameManager(CoinFlipChooseActivity.this);
        listOfChildren = gameManager.getNextChildrenToPick();

        ArrayList<String> children = (ArrayList<String>) EditChildActivity.getChildrenSharedPreferences(CoinFlipChooseActivity.this);
        if (listOfChildren.isEmpty() && !(children.isEmpty())){
            listOfChildren = children;
        }
        recyclerView = findViewById(R.id.listOfchildrenTochoose);
        setAdapters();
        setUpActionBar();
        setUpHistoryButton();
    }

    private void setUpHistoryButton() {
        Button history = findViewById(R.id.coinflip_history_button);
        history.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CoinFlipHistoryActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
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
    }

    public void setAdapters(){
        RecyclerViewChildrenPick adapter = new RecyclerViewChildrenPick(listOfChildren,CoinFlipChooseActivity.this);

        if (listOfChildren.isEmpty()) {
            adapter.goToToss("", "");
            finish();
        }
        else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
}