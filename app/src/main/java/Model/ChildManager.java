package Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Java class to support adding, editing, and deleting children using arraylist (singleton implementation)
public class ChildManager {
    private static final String CHILD_LIST = "childList";
    public static final String PATH_LIST = "pathList";
    private static final String PREFERENCES = "appPrefs";
    private ArrayList<Child> listOfChildren;
    private static ChildManager instance = null;

    private ChildManager(){
        listOfChildren = new ArrayList<>();
    }

    //singleton support
    public static ChildManager getInstance(){
        if(instance == null){
            instance = new ChildManager();
        }
        return instance;
    }

    public void addChild(String childName, Bitmap image, String filePath){
        if(childName.length() > 0 ){
            Child child = new Child(childName);
            child.setImage(image);
            child.setFilePath(filePath);
            listOfChildren.add(child);
        }
    }

    public void deleteChild(int index){
        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.remove(index);
        }
    }

    public void editChild(int index, String childName, Bitmap image, String filePath){
        if(index >= 0 && index < listOfChildren.size()){
            Child child = listOfChildren.get(index);
            child.setName(childName);
            child.setImage(image);
            child.setFilePath(filePath);
        }
    }

    public String getName(int index){
        return listOfChildren.get(index).getName();
    }

    public Child getChild(int index){
        if(index >= 0 && index < listOfChildren.size()){
            return listOfChildren.get(index);
        }
        return null;
    }

    public ArrayList<Child> getList(){
        return listOfChildren;
    }

    public void clearChildren(){
        listOfChildren.clear();
    }

    public void saveChildrenSharedPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(CHILD_LIST).apply();
        editor.remove(PATH_LIST).apply();
        StringBuilder childListString = new StringBuilder();
        StringBuilder imagePathString = new StringBuilder();

        for(int i = 0; i < listOfChildren.size(); i++){
            childListString.append(getName(i)).append(",");
            imagePathString.append(getChild(i).getFilePath()).append(",");
        }
        editor.putString(CHILD_LIST, childListString.toString());
        editor.putString(PATH_LIST, imagePathString.toString());
        editor.apply();
    }

    public List<String> getChildNameSharedPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String temp = "";
        String childListString = prefs.getString(CHILD_LIST, temp);
        List<String> childList = new ArrayList<>(Arrays.asList(childListString.split(",")));
        //from https://stackoverflow.com/questions/7488643/how-to-convert-comma-separated-string-to-list
        if(childList.get(0).equals("") && (childList.size() == 1)){
            childList.remove(0);
        }
        return childList;
    }

    public List<String> getFilePathSharedPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String temp = "";
        String filePathString = prefs.getString(PATH_LIST, temp);
        List<String> pathList = new ArrayList<>(Arrays.asList(filePathString.split(",")));
        //from https://stackoverflow.com/questions/7488643/how-to-convert-comma-separated-string-to-list
        if(pathList.get(0).equals("") && (pathList.size() == 1)){
            pathList.remove(0);
        }
        return pathList;
    }

    public ArrayList<Child> getListOfChildObjects(Context context){
        ArrayList<Child> listOfChildObjects = new ArrayList<>();
        List<String> listOfNames= getChildNameSharedPreferences(context);
        List<String> listOfFilePaths= getFilePathSharedPreferences(context);
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
        listOfChildren = listOfChildObjects;
        return listOfChildObjects;
    }

    public void clearChildrenSharedPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().apply();
    }
}
