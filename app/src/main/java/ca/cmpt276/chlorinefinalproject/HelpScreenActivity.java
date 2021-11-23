package ca.cmpt276.chlorinefinalproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import ca.cmpt276.chlorinefinalproject.databinding.ActivityHelpScreenBinding;
//Help screen showing citations (supporting hyperlinks), developer list and features implemented
public class HelpScreenActivity extends AppCompatActivity {
    private ActivityHelpScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);

        setUpToolBar();
        setupHyperLink();
    }

    private void setUpToolBar(){
        binding = ActivityHelpScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.helpTitle);
    }

    private void setupHyperLink() {
        TextView citationLink = findViewById(R.id.citationLinks);
        citationLink.setMovementMethod(LinkMovementMethod.getInstance());
        citationLink.setLinkTextColor(Color.BLUE);
    }


}