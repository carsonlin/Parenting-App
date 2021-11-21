package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import Model.TaskChild;
import Model.TaskListAdapter;

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
        else if (id == R.id.addChild){
            // add new task block
        }
        else if (id == R.id.clearChildren){
            // delete all tasks. not sure if we even need to implement this tbh
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListView(){

        ArrayList<TaskChild> tasks = new ArrayList<>();

        //placeholder stuff for testing
        tasks.add(new TaskChild("Take out the garbage", "Johnny"));
        tasks.add(new TaskChild("Sit in the front seat", "Carson"));
        tasks.add(new TaskChild("Choose the TV show", "Jack"));
        tasks.add(new TaskChild("Build the help screen", "Cuthbert"));

        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.tasks_layout, tasks);
        TaskListAdapter adapter = new TaskListAdapter(this, R.layout.adapter_view_layout_tasks, tasks);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_listofchildren, menu);
        return true;
    }



}