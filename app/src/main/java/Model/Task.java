package Model;

import java.util.ArrayList;

//task model class that holds Name of task and supports task completion (updating next child)
public class Task {
    private String taskName;
    private String child; //currently a string, needs to be child class
    private int childIndex;
    private final ArrayList<String> listOfChildren;   //build new list of children from shared preferences every time activity clicked?

    Task(String newTaskName, String newChild, ArrayList<String> newListOfChildren){
        taskName = newTaskName;
        child = newChild;
        listOfChildren = newListOfChildren;
        childIndex = listOfChildren.indexOf(child);
    }

    Task(String newTaskName, ArrayList<String> newListOfChildren){
        taskName = newTaskName;
        childIndex = 0;
        listOfChildren = newListOfChildren;
        child = listOfChildren.get(childIndex);
    }

    public void setTaskName(String newTaskName){
        taskName = newTaskName;
    }

    public void setTaskChild(String newChild){
        child = newChild;
    }

    public String getTaskName(){
        return taskName;
    }

    public String getTaskChild(){
        return child;
    }

    public void taskCompleted(){
        if(childIndex == listOfChildren.size() - 1){
            childIndex = 0;
        }
        else{
            childIndex++;
        }
        child = listOfChildren.get(childIndex);
    }
}
