package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import Model.Child;
import Model.ChildManager;
import Model.Task;
import Model.TaskChild;
import Model.TaskListAdapter;
import Model.TaskManager;

// Activity that shows the list of tasks and the child who's turn it is
public class WhoseTurnActivity extends AppCompatActivity {
    TaskManager taskManager = TaskManager.getInstance();
    ChildManager childManager = ChildManager.getInstance();

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
    protected void onResume() {
        super.onResume();
        setupListView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        else if (id == R.id.addChild){
            Intent intent = new Intent(WhoseTurnActivity.this, AddTaskActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.clearChildren){
            taskManager.deleteAllTasks();
            taskManager.saveToSharedPreferences(this);
            setupListView();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListView(){
        ArrayList<Task> tasks = taskManager.getTasks();
        ArrayList<TaskChild> taskChildrenList = new ArrayList<>();
        ArrayList<Child> children = childManager.getChildren();

        for (Task task : tasks){
            if (task.hasChild()){
                taskChildrenList.add(new TaskChild(task.getTaskName(), children.get(task.getChildIndex()).getName()));
            }
            else{
                taskChildrenList.add(new TaskChild(task.getTaskName(), ""));
            }
        }
        TaskListAdapter adapter = new TaskListAdapter(this, R.layout.adapter_view_layout_tasks, taskChildrenList);

        ListView list = findViewById(R.id.taskListView);
        list.setAdapter(adapter);
    }

    private void setupClickGameOnList(){
        ListView list = findViewById(R.id.taskListView);
        list.setOnItemClickListener((parent, viewClicked, position, id) -> {
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