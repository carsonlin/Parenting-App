package ca.cmpt276.chlorinefinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import Model.Child;
import Model.ChildManager;
import Model.TaskManager;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityEditOrDeleteChildBinding;

// Activity used to edit, delete, or add children.
public class EditChildActivity extends AppCompatActivity {
    private ActivityEditOrDeleteChildBinding binding;

    private static final String EXTRA_MESSAGE_ACTIVITY= "Extra - message";
    public static final String LIST_POSITION = "list position";
    private boolean isAddActivity;
    private int position;
    private boolean isImageUpdated = false;
    private ChildManager childManager;
    private Bitmap imageBitmap;

    public static Intent getAddOrDeleteChildIntent(Context c, boolean isAddActivity, int position){
        Intent intent = new Intent(c, EditChildActivity.class);
        intent.putExtra(EXTRA_MESSAGE_ACTIVITY, isAddActivity);
        intent.putExtra(LIST_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditOrDeleteChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        childManager = ChildManager.getInstance();
        setUpUI();
        setUpActionBar();
        deleteButtonPressed();
        saveButtonPressed();
        uploadImagePressed();
        takePhotoPressed();
    }

    private void setUpUI(){
        Intent intent = getIntent();
        position = intent.getIntExtra(LIST_POSITION, 0);
        EditText editText = findViewById(R.id.editChildName);
        ImageView imageView = findViewById(R.id.childProfilePic);
        isAddActivity = intent.getBooleanExtra(EXTRA_MESSAGE_ACTIVITY, true);

        if (isAddActivity){
            Button button = findViewById(R.id.deleteButton);
            button.setVisibility(View.GONE);
            editText.setText("");
            imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
        }
        else{
            editText.setText(childManager.getName(position));
            imageBitmap = childManager.getChild(position).getImage();
        }
        Glide.with(getApplicationContext()).load(imageBitmap).into(imageView);
    }

    private void setUpActionBar(){
        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        if(isAddActivity){
            ab.setTitle("Add a Child");
        }
    }

    private void deleteButtonPressed(){
        Button button = findViewById(R.id.deleteButton);
        button.setOnClickListener(view -> {
            childManager.deleteChild(position);
            childManager.saveChildrenSharedPreferences(this);
            finish();
        });
    }

    private void saveButtonPressed(){
        Button button = findViewById(R.id.saveButton);
        button.setOnClickListener(view -> {
            EditText ET = findViewById(R.id.editChildName);
            String text = ET.getText().toString();
            if (text.length() <= 0){
                Toast.makeText(EditChildActivity.this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
            }
             else {
                String path;
                if (isAddActivity){
                    path = saveToInternalStorage(imageBitmap, randomIdentifier() + ".jpg");
                    childManager.addChild(text, imageBitmap, path);

                    if (childManager.getNumberOfChildren() == 1){ // this assigns all the task without children when the first child is added
                        TaskManager taskManager = TaskManager.getInstance();
                        taskManager.setAllTaskIndexZero();
                    }
                }
                else {
                    Child child = childManager.getChild(position);

                    if(isImageUpdated){
                        deleteExistingFile(child.getFilePath());
                    }
                    path = saveToInternalStorage(imageBitmap, randomIdentifier() + ".jpg");
                    childManager.editChild(position, text, imageBitmap, path);
                }
                childManager.saveChildrenSharedPreferences(this);
                finish();
            }
        });
    }

    public void deleteExistingFile(String path){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("profileImageDir", Context.MODE_PRIVATE);
        try {
            File filePath = new File(path);
            String filename = filePath.getName();
            filePath = new File(directory, filename);

            if(filePath.exists()){
                filePath.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadImagePressed() {
        // Launches gallery for user to select image
        ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        Bitmap map = null;
                        try {
                            map = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageBitmap = map;
                        isImageUpdated = true;
                        ImageView imageView = findViewById(R.id.childProfilePic);
                        Glide.with(getApplicationContext()).load(result).into(imageView);
                    }
                });

        Button button = findViewById(R.id.uploadImage);
        button.setOnClickListener(view -> getContent.launch("image/*"));
    }

    private void takePhotoPressed() {
        ActivityResultLauncher<Intent> launchCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            imageBitmap = (Bitmap) bundle.get("data");
                            ImageView imageView = findViewById(R.id.childProfilePic);
                            Glide.with(getApplicationContext()).load(imageBitmap).into(imageView);
                        }
                    }
                });

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Button button = findViewById(R.id.takePhoto);
        button.setOnClickListener(view -> launchCamera.launch(intent));
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String filename) {
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir("profileImageDir", Context.MODE_PRIVATE);

        try {
            File filePath = new File(directory, filename);
            if (!filePath.createNewFile()){
                if(filePath.delete())
                    filePath = new File(directory, filename);
            }
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(filePath);
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    assert outputStream != null;
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filePath.getAbsolutePath();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "";
        }
    }

    private String randomIdentifier(){
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
            StringBuilder builder = new StringBuilder();
            while(builder.toString().length() == 0) {
                int length = rand.nextInt(5)+5;
                for(int i = 0; i < length; i++) {
                    builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
                }
            }
            return builder.toString();
    }
}