package ca.cmpt276.chlorinefinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.Child;
import Model.ChildManager;
import ca.cmpt276.chlorinefinalproject.databinding.ActivityEditOrDeleteChildBinding;

// Activity used to edit, delete, or add children.
public class EditChildActivity extends AppCompatActivity {
    private ActivityEditOrDeleteChildBinding binding;

    private static final String EXTRA_MESSAGE_ACTIVITY= "Extra - message";
    public static final String LIST_POSITION = "list position";
    private static final String CHILD_LIST = "childList";
    public static final String PATH_LIST = "pathList";
    private static final String PREFERENCES = "appPrefs";
    private boolean isAddActivity;
    private int position;
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
        imageView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap,
                imageView.getMaxWidth(),
                imageView.getMaxHeight(),
                false));
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
            saveChildrenSharedPreferences();
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
                if (isAddActivity){
                    String path = saveToInternalStorage(imageBitmap, randomIdentifier() + ".jpg");
                    childManager.addChild(text, imageBitmap, path);
                }
                else {
                    Child child = childManager.getChild(position);
                    String path;

                    // Avoid saving duplicate image if unchanged
                    if (imageBitmap == child.getImage()){
                        path = child.getFilePath();
                    }
                    else {
                        path = saveToInternalStorage(imageBitmap, randomIdentifier() + ".jpg");
                    }
                    childManager.editChild(position, text, imageBitmap, path);
                }
                saveChildrenSharedPreferences();
                finish();
            }
        });
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
                        ImageView imageView = findViewById(R.id.childProfilePic);
                        imageView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap,
                                imageView.getMaxWidth(),
                                imageView.getMaxHeight(),
                                false));
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
                            imageView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap,
                                    imageView.getMaxWidth(),
                                    imageView.getMaxHeight(),
                                    false));
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

    public void saveChildrenSharedPreferences(){
        SharedPreferences prefs = this.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(CHILD_LIST).apply();
        editor.remove(PATH_LIST).apply();
        StringBuilder childListString = new StringBuilder();
        StringBuilder imagePathString = new StringBuilder();

        for(int i = 0; i < childManager.getListSize(); i++){
            childListString.append(childManager.getName(i)).append(",");
            imagePathString.append(childManager.getChild(i).getFilePath()).append(",");
        }
        editor.putString(CHILD_LIST, childListString.toString());
        editor.putString(PATH_LIST, imagePathString.toString());
        editor.apply();
    }

    public static List<String> getChildNameSharedPreferences(Context context){
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

    public static List<String> getFilePathSharedPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        String temp = "";
        String filePathString = prefs.getString(PATH_LIST, temp);
        List<String> pathList = new ArrayList<>(Arrays.asList(filePathString.split(",")));
        //from https://stackoverflow.com/questions/7488643/how-to-convert-comma-separated-string-to-list
        if(pathList.get(0).equals("") && (pathList.size() == 1)){
            pathList.remove(0);
        }
        return pathList;
    }

    public ArrayList<Child> getListOfChildObjects(){
        ArrayList<Child> listOfChildObjects = new ArrayList<>();
        List<String> listOfNames= getChildNameSharedPreferences(this);
        List<String> listOfFilePaths= getFilePathSharedPreferences(this);
        for(int i = 0; i < listOfNames.size(); i++){
            Child child = new Child(listOfNames.get(i));
            child.setFilePath(listOfFilePaths.get(i));
            File file = new File(child.getFilePath());
            try {
                child.setImage(BitmapFactory.decodeStream(new FileInputStream(file)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            listOfChildObjects.add(child);
        }
        return listOfChildObjects;
    }

    public static void clearChildrenSharedPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().apply();
    }
}