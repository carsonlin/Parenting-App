package ca.cmpt276.chlorinefinalproject;



import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import Model.ChildListAdapter;
import Model.ChildManager;
import Model.TaskManager;
import Model.TurnHistoryManager;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityListOfChildrenBinding;

// Activity to display currently configured list of children
public class ListOfChildrenActivity extends AppCompatActivity {
    private ActivityListOfChildrenBinding binding;
    private ChildManager children;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListOfChildrenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        children = ChildManager.getInstance();
        setUpActionBar();
        populateListView();
        registerClickCallback();
    }

    private void populateListView() {
        ChildListAdapter adapter = new ChildListAdapter(this, R.layout.adapter_view_layout_children, children.getChildren());
        ListView list = findViewById(R.id.childListView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback(){
        ListView list = findViewById(R.id.childListView);
        list.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent i = EditChildActivity.getAddOrDeleteChildIntent(ListOfChildrenActivity.this, false, position);
            startActivity(i);
        });
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_listofchildren, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId() == R.id.addChild){
            Intent i = EditChildActivity.getAddOrDeleteChildIntent(ListOfChildrenActivity.this, true, -1);
            startActivity(i);
            return true;
        }
        else if(item.getItemId() == R.id.clearChildren){
            Toast.makeText(this, "Children List Cleared", Toast.LENGTH_SHORT).show();
            children.clearChildren();
            children.clearChildrenSharedPreferences(this);
            populateListView();

            TaskManager taskManager = TaskManager.getInstance();
            taskManager.updateTasksOnChildDelete(0, 0);
            taskManager.saveToSharedPreferences(this);

            TurnHistoryManager turnManager = TurnHistoryManager.getInstance();
            turnManager.updateTurnHistoryOnChildDelete(0, 0);

            return true;
        }
        else if (item.getItemId() == R.id.home){
            finish();
            return true;
        }
        return false;
    }

    protected void onResume(){
        super.onResume();
        populateListView();
    }
}