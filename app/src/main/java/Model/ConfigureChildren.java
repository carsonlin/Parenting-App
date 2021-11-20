package Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

// Java class to support adding, editing, and deleting children using arraylist (singleton implementation)
public class ConfigureChildren {
    private final ArrayList<Child> listOfChildren;
    private final ArrayList<String> listOfFilePaths;
    private static ConfigureChildren instance = null;

    private ConfigureChildren(){
        listOfChildren = new ArrayList<>();
        listOfFilePaths = new ArrayList<>();
    }

    //singleton support
    public static ConfigureChildren getInstance(){
        if(instance == null){
            instance = new ConfigureChildren();
        }
        return instance;
    }

    public void addChild(String childName, Bitmap image){
        if(childName.length() > 0 ){
            Child child = new Child(childName);
            child.setImage(image);
            listOfChildren.add(child);
        }
    }

    public void deleteChild(int index){
        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.remove(index);
        }
    }
    public void editChildName(int index, String childName){
        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.get(index).setName(childName);
        }
    }

    public String getChildName(int index){
        return listOfChildren.get(index).getName();
    }

    public Child getChildObject(int index){
        if(index >= 0 && index < listOfChildren.size()){
            return listOfChildren.get(index);
        }
        return null;
    }

    public void addFilePath(String filePath){
        listOfFilePaths.add(filePath);
    }

    public void deleteFilePath(int index){
        if(index >= 0 && index < listOfChildren.size()){
            listOfFilePaths.remove(index);
        }
    }

    public void editFilePath(int index, String filePath){
        if(index >= 0 && index < listOfChildren.size()){
            listOfFilePaths.set(index, filePath);
        }
    }

    public String getFilePath(int index){
        return listOfFilePaths.get(index);
    }

    public int getListSize(){
        return listOfChildren.size();
    }

    public void clearChildren(){
        listOfChildren.clear();
    }

    public void clearFilePaths(){
        listOfFilePaths.clear();
    }
}
