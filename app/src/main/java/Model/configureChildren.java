package Model;

import java.util.ArrayList;

//Java class to support adding, editing, and deleting children (singleton implementation)
public class configureChildren {
    private ArrayList<String> listOfChildren;
    private static configureChildren instance = null;


    private configureChildren(){
        listOfChildren = new ArrayList<>();
    }
    //singleton support
    public static configureChildren getInstance(){
        if(instance == null){
            instance = new configureChildren();
        }
        return instance;
    }

    public void addChild(String childName){
        if(childName.length() > 0 ){
            listOfChildren.add(childName);
        }
    }
    public void deleteChild(int index){
        if(index > 0 && index < listOfChildren.size()){
            listOfChildren.remove(index);
        }
    }
    public void editChild(int index,String childName){
        if(index > 0 && index < listOfChildren.size()){
            listOfChildren.set(index, childName);
        }
    }

    public String getChild(int index){
        return listOfChildren.get(index);
    }

    public int getListSize(){
        return listOfChildren.size();
    }
}
