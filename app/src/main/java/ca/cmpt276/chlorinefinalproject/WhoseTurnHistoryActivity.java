package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import Model.AdapterManager;
import Model.TaskManager;
import Model.TurnHistoryManager;

//Activity for seeing the turn history a certain task
public class WhoseTurnHistoryActivity extends AppCompatActivity {
    private final TurnHistoryManager turnManager = TurnHistoryManager.getInstance();
    private final TaskManager taskManager = TaskManager.getInstance();
    private String taskName;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whose_turn_history);
        setupToolbar();
        recyclerView = findViewById(R.id.taskHistoryRecycler);
        setAdapters();
        extractDataFromIntent();

        AdapterManager adapterManager = AdapterManager.getInstance();
        adapterManager.updateDataSet(turnManager.getSingleTaskHistory(taskName));
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.turn_history);
    }

    public static Intent makeIntent(Context context, int taskSelected){ //these two methods are from Dr. Fraser's Youtube tutorial
        Intent intent = new Intent(context, WhoseTurnHistoryActivity.class);
        intent.putExtra(ViewTaskActivity.TASK_INDEX, taskSelected);
        return intent;
    }

    private void extractDataFromIntent() {
        Intent intent = getIntent();
        int taskIndex = intent.getIntExtra(ViewTaskActivity.TASK_INDEX, -1);
        taskName = taskManager.getTask(taskIndex).getTaskName();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapters(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(AdapterManager.getInstance().getAdapter());
    }
}