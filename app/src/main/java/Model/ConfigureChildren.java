package Model;

import java.util.ArrayList;

// Java class to support adding, editing, and deleting children using arraylist (singleton implementation)
public class ConfigureChildren {
    private final ArrayList<Child> listOfChildren;
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
            listOfChildren.add(new Child(childName));
        }
    }
    public void deleteChild(int index){
        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.remove(index);
        }
    }
    public void editChild(int index,String childName){
        if(index >= 0 && index < listOfChildren.size()){
            listOfChildren.get(index).setName(childName);
        }
    }

    public void clearChildren(){
        listOfChildren.clear();
    }

    public String getChild(int index){
        return listOfChildren.get(index).getName();
    }

    public int getListSize(){
        return listOfChildren.size();
    }
}
