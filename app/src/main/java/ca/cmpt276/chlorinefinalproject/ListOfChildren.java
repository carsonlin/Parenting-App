package ca.cmpt276.chlorinefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import Model.ConfigureChildren;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityListOfChildrenBinding;

// Activity uses ConfigureChildren class to save and retrieve from shared preferences
public class ListOfChildren extends AppCompatActivity {
    private ActivityListOfChildrenBinding binding;

    private ConfigureChildren children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListOfChildrenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        children = ConfigureChildren.getInstance();
        setUpActionBar();
        populateListView();
        registerClickCallback();
    }

    private void populateListView() {
        ArrayList<String> childrenListView = new ArrayList<>();

        for(int i = 0; i < children.getListSize(); i++){
            childrenListView.add(children.getChild(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.listitems,
                childrenListView);
        ListView list = findViewById(R.id.childListView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback(){
        ListView list = findViewById(R.id.childListView);
        list.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent i = EditOrDeleteChild.getAddOrDeleteChildIntent(ListOfChildren.this, "edit", position);
            startActivity(i);
        });
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_listofchildren, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.addChild){
            Toast.makeText(this, "Add a child", Toast.LENGTH_SHORT).show();
            Intent i = EditOrDeleteChild.getAddOrDeleteChildIntent(ListOfChildren.this, "add", -1);
            startActivity(i);
            return true;
        }
        else if (item.getItemId() == R.id.home){
            finish();
            return true;
        }
        return false;
    }

    protected void onResume(){
        super.onResume();
        populateListView();
    }
}