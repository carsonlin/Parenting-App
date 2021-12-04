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
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;

import Model.ChildManager;
import Model.RecyclerViewCoinFlipHistory;
import Model.RecyclerViewTurnHistory;
import Model.Task;
import Model.TaskManager;
import Model.TurnHistory;
import Model.TurnHistoryManager;

public class WhoseTurnHistoryActivity extends AppCompatActivity {
    TurnHistoryManager turnManager = TurnHistoryManager.getInstance();
    ChildManager childManager = ChildManager.getInstance();
    TaskManager taskManager = TaskManager.getInstance();
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
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Turn History");
    }

    public static Intent makeIntent(Context context, int taskSelected){
        Intent intent = new Intent(context, WhoseTurnHistoryActivity.class);
        intent.putExtra(ViewTaskActivity.TASK_INDEX, taskSelected);
        return intent;
    }

    private void extractDataFromIntent() { //these two methods are from Dr. Fraser's Youtube tutorial
        Intent intent = getIntent();
        int taskIndex = intent.getIntExtra(ViewTaskActivity.TASK_INDEX, -1);
        taskName = taskManager.getTask(taskIndex).getTaskName();

        Log.d("IDK", "This is the task name passed to the recycler view " + taskName);
        Log.d("IDK", "Length of turnmanager list? " + turnManager.getSingleTaskHistory(taskName).size());

        for (TurnHistory turnHistory : turnManager.getSingleTaskHistory(taskName)){
            Log.d("IDK", "this is the turnhistory name " + turnHistory.getTaskName() + " " + turnHistory.getChildIndex());
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setAdapters(){
        //Log.d("IDK", "" + taskName.equals("oregano"))
        RecyclerViewTurnHistory adapter = new RecyclerViewTurnHistory(childManager, turnManager.getSingleTaskHistory(taskName), getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}