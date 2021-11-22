package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
    public static final String OVERRIDE = "override";
    private ActivityCoinFlipChooseBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<String> listOfChildren;
    private GameManager gameManager;
    private RecyclerViewChildrenPick adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_choose);
        binding = ActivityCoinFlipChooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gameManager = new GameManager(CoinFlipChooseActivity.this);

        if (isOverride()) {
            listOfChildren = gameManager.getQueueOfNewChildrenList();
        } else {
            listOfChildren = gameManager.getNextChildrenToPick();

            ArrayList<String> children = (ArrayList<String>) EditChildActivity.getChildNameSharedPreferences(CoinFlipChooseActivity.this);
            if (listOfChildren.isEmpty() && !(children.isEmpty())) {
                listOfChildren = children;
            }
        }

        recyclerView = findViewById(R.id.listOfchildrenTochoose);
        adapter = new RecyclerViewChildrenPick(listOfChildren,CoinFlipChooseActivity.this);
        setAdapters();
        setUpActionBar();
        setUpButtons();
        setUpVisibleButtons();

    }

    private void setUpButtons() {
        Button history = findViewById(R.id.coinflip_history_button);
        Button chooseNoneButton = findViewById(R.id.coinflip_choose_none_button2);
        Button override = findViewById(R.id.coinflip_overide_button);
        override.setOnClickListener(view -> override());
        history.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CoinFlipHistoryActivity.class);
            startActivity(intent);
            finish();
        });
        chooseNoneButton.setOnClickListener(view -> navigateWithNoChildchoosen());
    }

    private void setUpVisibleButtons(){

        Button override = findViewById(R.id.coinflip_overide_button);
        Button historyButton = findViewById(R.id.coinflip_history_button);
        Button chooseNoneButton = findViewById(R.id.coinflip_choose_none_button2);

        if (isOverride()) {
            override.setVisibility(View.GONE);
            chooseNoneButton.setVisibility(View.VISIBLE);
            historyButton.setVisibility(View.GONE);
        }
        else{
            override.setVisibility(View.VISIBLE);
            chooseNoneButton.setVisibility(View.GONE);
            historyButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }

    private void override() {
        Intent intent = new Intent(getBaseContext(), CoinFlipChooseActivity.class);
        intent.putExtra(OVERRIDE, true);
        startActivity(intent);
    }

    private boolean isOverride() {
        return getIntent().getBooleanExtra(OVERRIDE, false);
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void navigateWithNoChildchoosen(){
        adapter.goToToss("", "");
        finish();
    }

    public void setAdapters(){
        if (listOfChildren.isEmpty()) {
            navigateWithNoChildchoosen();
        }
        else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
}