package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewTaskActivity extends AppCompatActivity {

    public static final String TASK_INDEX = "Task selected";
    int taskIndex;
    Boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        extractDataFromIntent();
        setupToolbar();
        setupTurnCompleteButton();
        setupEditButton();
        setEditMode(false);

        // use taskIndex to get the right task from the taskManager

    }

    public static Intent makeIntent(Context context, int taskSelected){
        Intent intent = new Intent(context, ViewTaskActivity.class);
        intent.putExtra(TASK_INDEX, taskSelected);
        return intent;
    }

    private void extractDataFromIntent() { //these two methods are from Dr. Fraser's Youtube tutorial
        Intent intent = getIntent();
        taskIndex = intent.getIntExtra(TASK_INDEX, -1);
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

            //Code here for advancing current child to next child in round robin!

        });
    }

    private void setupEditButton(){
        Button button = findViewById(R.id.editButton);

        button.setOnClickListener(view -> toggleEditMode());
    }

    private void setEditMode(Boolean edit){
        TextView taskName = findViewById(R.id.taskDesc);
        EditText editText = findViewById(R.id.editTaskName);

        if (edit){
            taskName.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.VISIBLE);
        }
        else {
            taskName.setVisibility(View.VISIBLE);
            editText.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleEditMode(){
        setEditMode(!editMode);
        editMode = !editMode;
    }

    private void setupSaveButton(){
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(view -> {

            // code for saving change to task name

            setContentView(R.layout.activity_view_task);

        });
    }

    private void setupDeleteButton(){
        Button saveButton = findViewById(R.id.deleteButton);

        saveButton.setOnClickListener(view -> {

            // code for deleting task

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}