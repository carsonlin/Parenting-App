package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Model.Task;
import Model.TaskManager;

// Activity to add a new task
public class AddTaskActivity extends AppCompatActivity {
    TaskManager taskManager = TaskManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_child);
        setupToolbar();
        renameViews();
        setupSaveButton();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.add_task);
    }

    private void renameViews(){
        TextView taskName = findViewById(R.id.childName);
        taskName.setText(R.string.task_name);

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.INVISIBLE);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setText(R.string.save);

        Button uploadButton = findViewById(R.id.uploadImage);
        uploadButton.setVisibility(View.INVISIBLE);

        Button takePhotoButton = findViewById(R.id.takePhoto);
        takePhotoButton.setVisibility(View.INVISIBLE);

        EditText newTaskEditText = findViewById(R.id.editChildName);
        newTaskEditText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    private void setupSaveButton(){
        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(view -> {
            EditText newTaskEditText = findViewById(R.id.editChildName);
            String newTaskName = newTaskEditText.getText().toString();

            Task newTask = new Task(newTaskName);
            taskManager.addTask(newTask);
            taskManager.saveToSharedPreferences(this);

            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            finish();
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