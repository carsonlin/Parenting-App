package ca.cmpt276.chlorinefinalproject;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Model.RecyclerViewChildrenPick;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipChooseBinding;

public class CoinFlipChooseActivity extends AppCompatActivity {
    private ActivityCoinFlipChooseBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<String> listOfChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_choose);
        binding = ActivityCoinFlipChooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listOfChildren = (ArrayList<String>) EditChildActivity.getChildrenSharedPreferences(CoinFlipChooseActivity.this);
        recyclerView = findViewById(R.id.listOfchildrenTochoose);
        setAdapters();
        setUpActionBar();
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
        ab.setTitle(" pick child to flip ");
    }

    public void setAdapters(){
        RecyclerViewChildrenPick adapter = new RecyclerViewChildrenPick(listOfChildren,getApplicationContext());

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