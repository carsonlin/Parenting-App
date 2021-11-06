package ca.cmpt276.chlorinefinalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import androidx.navigation.ui.AppBarConfiguration;


import Model.configureChildren;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityEditOrDeleteChildBinding;

//activity used to edit, delete, or add children.
public class editOrDeleteChild extends AppCompatActivity {
    private ActivityEditOrDeleteChildBinding binding;

    private static String EXTRA_MESSAGE_ACTIVITY= "Extra - message";
    private String activityName;
    private int position;
    private configureChildren children = configureChildren.getInstance();

    public static Intent getAddOrDeleteChildIntent(Context c,String activity, int position){
        Intent intent = new Intent(c, editOrDeleteChild.class);
        intent.putExtra(EXTRA_MESSAGE_ACTIVITY, activity);
        intent.putExtra("list position", position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditOrDeleteChildBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        setUpUI();
        setUpActionBar();
        deleteButtonPressed();
        saveButtonPressed();
    }

    private void setUpUI(){
        Intent i = getIntent();
        position = i.getIntExtra("list position", 0);
        EditText ET = findViewById(R.id.editChildName);
        activityName = i.getStringExtra(EXTRA_MESSAGE_ACTIVITY);
        if(activityName.equals("add")){
            Button button = findViewById(R.id.deleteButton);
            button.setVisibility(View.GONE);
            ET.setText("");
        }
        if(activityName.equals("edit")){
            ET.setText(children.getChild(position));
        }
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        if(activityName.equals("add")){
            ab.setTitle("Add a Child");
        }
    }

    private void deleteButtonPressed(){
        Button button = findViewById(R.id.deleteButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                children.deleteChild(position);
                finish();
            }
        });
    }

    private void saveButtonPressed(){
        Button button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ET = findViewById(R.id.editChildName);
                String text = ET.getText().toString();
                if(text.length() <= 0){
                    Toast.makeText(editOrDeleteChild.this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(activityName.equals("add")){
                    children.addChild(text);
                    finish();
                }
                if(activityName.equals("edit")){
                    children.editChild(position, text);
                    finish();
                }
            }
        });
    }


}