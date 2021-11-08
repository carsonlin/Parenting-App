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

import Model.GameManager;
import Model.RecyclerViewChildrenPick;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityListOfChildrenToChooseBinding;

public class ListOfChildrenToChoose extends AppCompatActivity {
    private ActivityListOfChildrenToChooseBinding binding;
    private GameManager gameManager;
    private RecyclerView recyclerView;
    private ArrayList<String> children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_children_to_choose);
        binding = ActivityListOfChildrenToChooseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameManager = new GameManager(ListOfChildrenToChoose.this);
        children = gameManager.getNextChildrenToPick();
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
        RecyclerViewChildrenPick adapter = new RecyclerViewChildrenPick(children,getApplicationContext());

        if (children.isEmpty()) {
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