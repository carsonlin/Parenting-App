package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import Model.ChildManager;
import Model.Task;
import Model.TaskManager;

// Activity to view task. From here you can edit the name of the task, or mark the task as complete
public class ViewTaskActivity extends AppCompatActivity {
    public static final String TASK_INDEX = "Task selected";
    int taskIndex;
    Boolean editMode = false;
    TaskManager taskManager = TaskManager.getInstance();
    ChildManager childManager = ChildManager.getInstance();
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        extractDataFromIntent();
        setupToolbar();
        setupTurnCompleteButton();
        setupEditButton();
        setEditMode(false);
        populateTextViews();
        setupChildImage();
    }

    public static Intent makeIntent(Context context, int taskSelected){
        Intent intent = new Intent(context, ViewTaskActivity.class);
        intent.putExtra(TASK_INDEX, taskSelected);
        return intent;
    }

    private void extractDataFromIntent() { //these two methods are from Dr. Fraser's Youtube tutorial
        Intent intent = getIntent();
        taskIndex = intent.getIntExtra(TASK_INDEX, -1);
        task = taskManager.getTask(taskIndex);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.task);
    }

    private void setupTurnCompleteButton(){
        Button button = findViewById(R.id.taskCompleteButton);

        button.setOnClickListener(view -> {
            task.completeTask();
            taskManager.saveToSharedPreferences(this);
            finish();
        });
    }

    private void setupChildImage(){
        ImageView childImage = findViewById(R.id.childImage);
        if (task.hasChild()){
            Glide.with(getApplicationContext()).load(childManager.getChild(task.getChildIndex()).getImage()).into(childImage);
        }
    }

    private void setupEditButton(){
        Button button = findViewById(R.id.editButton);
        EditText editText = findViewById(R.id.editTaskName);

        button.setOnClickListener(view -> {
            if (editMode){
                String newTaskName = editText.getText().toString();
                task.setTaskName(newTaskName);
                taskManager.saveToSharedPreferences(this);
            }
            toggleEditMode();
        });
    }

    private void setEditMode(Boolean edit){
        TextView taskName = findViewById(R.id.taskDesc);
        EditText editText = findViewById(R.id.editTaskName);
        Button editButton = findViewById(R.id.editButton);

        if (edit){
            taskName.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.VISIBLE);
            editButton.setText(R.string.save_changes);
            editText.setText(task.getTaskName());

        }
        else {
            taskName.setVisibility(View.VISIBLE);
            editText.setVisibility(View.INVISIBLE);
            editButton.setText(R.string.edit_task);
            taskName.setText(task.getTaskName());
        }
    }

    private void toggleEditMode(){
        setEditMode(!editMode);
        editMode = !editMode;
    }

    public void populateTextViews(){
        TextView taskDescView = findViewById(R.id.taskDesc);
        TextView childNameView = findViewById(R.id.childName);
        taskDescView.setText(task.getTaskName());

        if (task.hasChild()) {
            childNameView.setText(childManager.getChild(task.getChildIndex()).getName());
        }
        else {
            childNameView.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        else if (id == R.id.delete_button){
            taskManager.removeTask(taskIndex);
            taskManager.saveToSharedPreferences(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}