package ca.cmpt276.chlorinefinalproject;


import static ca.cmpt276.chlorinefinalproject.EditChildActivity.clearChildrenSharedPreferences;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import Model.ChildListAdapter;
import Model.ChildManager;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityListOfChildrenBinding;

// Activity uses ChildManager class to save and retrieve from shared preferences
public class ListOfChildrenActivity extends AppCompatActivity {
    private ActivityListOfChildrenBinding binding;

    private ChildManager children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListOfChildrenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        children = ChildManager.getInstance();
        setUpActionBar();
        populateListView();
        registerClickCallback();
    }

    private void populateListView() {
        List<String> childList = EditChildActivity.getChildNameSharedPreferences(this);
        List<String> pathList = EditChildActivity.getFilePathSharedPreferences(this);
        children.clearChildren();
        for(int i = 0; i < childList.size(); i++){
            String path = pathList.get(i);
            Bitmap map = loadImageFromStorage(path);
            children.addChild(childList.get(i), map, path);
        }

        ChildListAdapter adapter = new ChildListAdapter(this, R.layout.adapter_view_layout_children, children.getList());
        ListView list = findViewById(R.id.childListView);
        list.setAdapter(adapter);
    }

    private Bitmap loadImageFromStorage(String path) {
        try {
            File file = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void registerClickCallback(){
        ListView list = findViewById(R.id.childListView);
        list.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent i = EditChildActivity.getAddOrDeleteChildIntent(ListOfChildrenActivity.this, false, position);
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
            Intent i = EditChildActivity.getAddOrDeleteChildIntent(ListOfChildrenActivity.this, true, -1);
            startActivity(i);
            return true;
        }
        else if(item.getItemId() == R.id.clearChildren){
            Toast.makeText(this, "Children List Cleared", Toast.LENGTH_SHORT).show();
            children.clearChildren();
            clearChildrenSharedPreferences(this);
            populateListView();
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