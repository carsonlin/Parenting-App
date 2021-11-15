package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class WhoseTurnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whose_turn);

        setupToolbar();
        setupListView();
        setupClickGameOnList();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListView(){

        ArrayList<String> tasks = new ArrayList<>();

        //placeholder stuff for testing
        tasks.add("Take out the garbage");
        tasks.add("Sit in the front seat");
        tasks.add("Choose the tv show");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.tasks_layout, tasks);

        ListView list = findViewById(R.id.taskListView);
        list.setAdapter(adapter);

    }

    private void setupClickGameOnList(){
        ListView list = findViewById(R.id.taskListView);
        list.setOnItemClickListener((parent, viewClicked, position, id) -> {

            Log.i("TEST", "CLICKED ON " + position);
            Intent intent = ViewTaskActivity.makeIntent(WhoseTurnActivity.this, position);
            startActivity(intent);
        });
    }

}