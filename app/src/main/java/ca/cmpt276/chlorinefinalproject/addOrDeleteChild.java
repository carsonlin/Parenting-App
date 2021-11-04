package ca.cmpt276.chlorinefinalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import androidx.navigation.ui.AppBarConfiguration;


import ca.cmpt276.chlorinefinalproject.databinding.ActivityAddOrDeleteChildBinding;

public class addOrDeleteChild extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAddOrDeleteChildBinding binding;

    private static final String EXTRA_MESSAGE = "Extra - message";

    public static Intent getAddOrDeleteChildIntent(Context c, int position){
        Intent intent = new Intent(c, addOrDeleteChild.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddOrDeleteChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
    }


}