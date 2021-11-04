package ca.cmpt276.chlorinefinalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import androidx.navigation.ui.AppBarConfiguration;


import ca.cmpt276.chlorinefinalproject.databinding.ActivityListOfChildrenBinding;

public class listOfChildren extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityListOfChildrenBinding binding;

    public static Intent getListOfChildrenIntent(Context c){
        Intent intent = new Intent(c, listOfChildren.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListOfChildrenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpActionBar();

    }

    //up button currently attached to main activity, change to menu activity when completed
    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_listofchildren, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.addChild:
                Toast.makeText(this, "Add a child", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.home:
                finish();
                return true;
        }
        return false;
    }
}