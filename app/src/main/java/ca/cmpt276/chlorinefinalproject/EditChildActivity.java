package ca.cmpt276.chlorinefinalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.ConfigureChildren;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityEditOrDeleteChildBinding;

// Activity used to edit, delete, or add children.
public class EditChildActivity extends AppCompatActivity {
    private ActivityEditOrDeleteChildBinding binding;

    private static final String EXTRA_MESSAGE_ACTIVITY= "Extra - message";
    private static final String CHILD_LIST = "childList";
    private static final String PREFERENCES = "appPrefs";
    private String activityName;
    private int position;
    private ConfigureChildren children;

    public static Intent getAddOrDeleteChildIntent(Context c,String activity, int position){
        Intent intent = new Intent(c, EditChildActivity.class);
        intent.putExtra(EXTRA_MESSAGE_ACTIVITY, activity);
        intent.putExtra("list position", position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditOrDeleteChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        children = ConfigureChildren.getInstance();
        setUpUI();
        setUpActionBar();
        deleteButtonPressed();
        saveButtonPressed();
    }

    private void setUpUI(){
        Intent intent = getIntent();
        position = intent.getIntExtra("list position", 0);
        EditText editText = findViewById(R.id.editChildName);
        activityName = intent.getStringExtra(EXTRA_MESSAGE_ACTIVITY);

        if(activityName.equals("add")){
            Button button = findViewById(R.id.deleteButton);
            button.setVisibility(View.GONE);
            editText.setText("");
        }

        if(activityName.equals("edit")){
            editText.setText(children.getChild(position));
        }
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        if(activityName.equals("add")){
            ab.setTitle("Add a Child");
        }
    }

    private void deleteButtonPressed(){
        Button button = findViewById(R.id.deleteButton);
        button.setOnClickListener(view -> {
            children.deleteChild(position);
            saveChildrenSharedPreferences();
            finish();
        });
    }

    private void saveButtonPressed(){
        Button button = findViewById(R.id.saveButton);
        button.setOnClickListener(view -> {
            EditText ET = findViewById(R.id.editChildName);
            String text = ET.getText().toString();
            if(text.length() <= 0){
                Toast.makeText(EditChildActivity.this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
                return;
            }
            if(activityName.equals("add")){
                children.addChild(text);
                saveChildrenSharedPreferences();
                finish();
            }
            if(activityName.equals("edit")){
                children.editChild(position, text);
                saveChildrenSharedPreferences();
                finish();
            }
        });
    }

    public void saveChildrenSharedPreferences(){
        SharedPreferences prefs = this.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(CHILD_LIST).apply();
        StringBuilder childListString = new StringBuilder();
        for(int i = 0; i < children.getListSize(); i++){
            childListString.append(children.getChild(i)).append(",");
        }
        editor.putString(CHILD_LIST, childListString.toString());
        editor.apply();
    }

    public static List<String> getChildrenSharedPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        String temp = "";
        String childListString = prefs.getString(CHILD_LIST, temp);
        List<String> childList = new ArrayList<>(Arrays.asList(childListString.split(",")));
        //from https://stackoverflow.com/questions/7488643/how-to-convert-comma-separated-string-to-list
        if(childList.get(0).equals("") && (childList.size() == 1)){
            childList.remove(0);
        }
        return childList;
    }
}