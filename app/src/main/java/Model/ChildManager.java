package Model;

import android.graphics.Bitmap;
import android.util.Log;

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

            if (listOfChildren.size() == 1){ // this assigns all the task without children when the first child is added
                TaskManager taskManager = TaskManager.getInstance();
                taskManager.setAllTaskIndexZero();
            }
        }
    }

    public void deleteChild(int index){
        TaskManager taskManager = TaskManager.getInstance();

        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.remove(index);
            taskManager.updateTasksOnChildDelete(index, listOfChildren.size());
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

    public int getListSize(){
        return listOfChildren.size();
    }

    public void clearChildren(){
        TaskManager taskManager = TaskManager.getInstance();
        taskManager.updateTasksOnChildDelete(0, 0);
        listOfChildren.clear();
    }
}
