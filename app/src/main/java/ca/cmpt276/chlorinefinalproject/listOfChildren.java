package ca.cmpt276.chlorinefinalproject;

import android.content.Context;
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


import androidx.navigation.ui.AppBarConfiguration;


import java.util.ArrayList;

import Model.configureChildren;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityListOfChildrenBinding;

public class listOfChildren extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityListOfChildrenBinding binding;

    private configureChildren children;

    public static Intent getListOfChildrenIntent(Context c){
        Intent intent = new Intent(c, listOfChildren.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListOfChildrenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        children = configureChildren.getInstance();
        setUpActionBar();
        populateListView();
        registerClickCallback();
    }

    private void populateListView() {
        ArrayList<String> childrenListView = new ArrayList<>();
//        for(int i = 0; i < children.getListSize(); i++){
//            childrenListView.add(children.getChild(i));
//        }
        childrenListView.add("1");
        childrenListView.add("2");
        childrenListView.add("3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.listitems,
                childrenListView);
        ListView list = (ListView) findViewById(R.id.childListView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback(){
        ListView list = (ListView) findViewById(R.id.childListView);
        list.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent i = addOrDeleteChild.getAddOrDeleteChildIntent(listOfChildren.this, position);
            startActivity(i);
        });
    }

    //up button currently attached to main activity, change to menu activity when completed
    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_listofchildren, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.addChild:
                Toast.makeText(this, "Add a child", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.home:
                finish();
                return true;
        }
        return false;
    }
}