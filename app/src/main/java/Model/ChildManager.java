package Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

// Java class to support adding, editing, and deleting children using arraylist (singleton implementation)
public class ChildManager {
    private final ArrayList<Child> listOfChildren;
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

    public int getListSize(){
        return listOfChildren.size();
    }

    public void clearChildren(){
        listOfChildren.clear();
    }
}
