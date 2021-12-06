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

// Java class to manage list of child objects, supports saving data to SharedPreferences
public class ChildManager {
    private static final String CHILD_LIST = "childList";
    public static final String PATH_LIST = "pathList";
    private static final String PREFERENCES = "appPrefs";
    private final ArrayList<Child> listOfChildren;
    private static ChildManager instance = null;

    private ChildManager(){
        listOfChildren = new ArrayList<>();
    }

    public static ChildManager getInstance(){
        if(instance == null){
            instance = new ChildManager();
        }
        return instance;
    }

    public void addChild(String childName, Bitmap image, String filePath){
        if(childName.length() > 0 ){
            Child child = new Child(childName, image, filePath);
            listOfChildren.add(child);
        }
    }

    public void deleteChild(int index){
        TaskManager taskManager = TaskManager.getInstance();
        TurnHistoryManager turnManager = TurnHistoryManager.getInstance();

        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.remove(index);
            taskManager.updateTasksOnChildDelete(index, listOfChildren.size());
            turnManager.updateTurnHistoryOnChildDelete(index, listOfChildren.size());
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
        return listOfChildren.get(index);
    }

    public ArrayList<Child> getChildren(){
        return listOfChildren;
    }

    public Boolean hasChildren(){
        return !listOfChildren.isEmpty();
    }

    public int getNumberOfChildren(){
        return listOfChildren.size();
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

    public void updateChildManager(Context context){
        List<String> listOfNames = getChildNameSharedPreferences(context);
        List<String> listOfFilePaths = getFilePathSharedPreferences(context);
        clearChildren();
        for(int i = 0; i < listOfNames.size(); i++){
            String name = listOfNames.get(i);
            String path = listOfFilePaths.get(i);
            Bitmap image = null;

            File file = new File(path);
            try {
                image = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            addChild(name, image, path);
        }
    }

    public void clearChildrenSharedPreferences(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear().apply();
    }
}
