package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

import Model.GameManager;
import Model.recyclerViewchildrenPick;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityCoinFlipBinding;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityListOfChildrenBinding;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityListOfChildrenToChooseBinding;

public class ListOfChildrenToChoose extends AppCompatActivity {
//ActivityListOfChildrenBinding

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
        gameManager = new GameManager(ListOfChildrenToChoose.this,null);
        children = gameManager.getNextChildrentoPick();
        recyclerView = (RecyclerView) findViewById(R.id.listOfchildrenTochoose);
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

        recyclerViewchildrenPick adapter = new recyclerViewchildrenPick(children,getApplicationContext());

        if (children.isEmpty()) {
            adapter.goTotoss("", "");
            finish();
        }else {

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

        }

    }


}