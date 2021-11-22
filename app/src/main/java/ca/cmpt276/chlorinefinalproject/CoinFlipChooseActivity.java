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

import Model.ChildManager;
import Model.GameManager;
import Model.RecyclerViewChildrenPick;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipChooseBinding;

// Activity to select a child for a coin flip
public class CoinFlipChooseActivity extends AppCompatActivity {
    public static final String OVERRIDE = "override";
    private ActivityCoinFlipChooseBinding binding;
    private RecyclerView recyclerView;
    private RecyclerViewChildrenPick adapter;
    private boolean isOverride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_choose);
        binding = ActivityCoinFlipChooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GameManager gameManager = new GameManager(CoinFlipChooseActivity.this);
        ChildManager childManager = ChildManager.getInstance();
        isOverride = getIntent().getBooleanExtra(OVERRIDE, false);

        recyclerView = findViewById(R.id.listOfchildrenTochoose);
        adapter = new RecyclerViewChildrenPick(isOverride, gameManager, childManager,CoinFlipChooseActivity.this);
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
        chooseNoneButton.setOnClickListener(view -> navigateWithNoChildChosen());
    }

    private void setUpVisibleButtons(){

        Button override = findViewById(R.id.coinflip_overide_button);
        Button historyButton = findViewById(R.id.coinflip_history_button);
        Button chooseNoneButton = findViewById(R.id.coinflip_choose_none_button2);

        if (isOverride) {
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

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void navigateWithNoChildChosen(){
        adapter.goToToss("", "");
        finish();
    }

    public void setAdapters(){
        if (adapter.getListOfChildren().isEmpty()) {
            navigateWithNoChildChosen();
        }
        else {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }
    }
}