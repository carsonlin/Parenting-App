package ca.cmpt276.chlorinefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ViewTaskActivity extends AppCompatActivity {

    public static final String TASK_INDEX = "Task selected";
    int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete_child);
        changeLayoutTextToTask();
        extractDataFromIntent();
        setupToolbar();
        setupSaveButton();
        setupEditText();

        // use taskIndex to get the right task from the taskManager

    }

    private void setupSaveButton(){
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {

            EditText editText = findViewById(R.id.editChildName);
            String taskName = editText.getText().toString();
            Log.i("SAVE TASK", "Saving " + taskName);

        });
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
        ab.setTitle(R.string.edit_task);
    }

    private void setupEditText() {
        EditText editText = findViewById(R.id.editChildName);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    private void changeLayoutTextToTask(){
        TextView textView = findViewById(R.id.childName);
        textView.setText(R.string.task_name);

        textView = findViewById(R.id.deleteButton);
        textView.setText(R.string.delete);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
//
//        return true;
//    }

}