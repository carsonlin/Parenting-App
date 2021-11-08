package Model;

import java.util.ArrayList;

// Java class to support adding, editing, and deleting children using arraylist (singleton implementation)
public class ConfigureChildren {
    private final ArrayList<String> listOfChildren;
    private static ConfigureChildren instance = null;

    private ConfigureChildren(){
        listOfChildren = new ArrayList<>();
    }

    //singleton support
    public static ConfigureChildren getInstance(){
        if(instance == null){
            instance = new ConfigureChildren();
        }
        return instance;
    }

    public void addChild(String childName){
        if(childName.length() > 0 ){
            listOfChildren.add(childName);
        }
    }
    public void deleteChild(int index){
        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.remove(index);
        }
    }
    public void editChild(int index,String childName){
        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.set(index, childName);
        }
    }

    public void clearChildren(){
        listOfChildren.clear();
    }

    public String getChild(int index){
        return listOfChildren.get(index);
    }

    public int getListSize(){
        return listOfChildren.size();
    }
}
