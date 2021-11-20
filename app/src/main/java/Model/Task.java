package Model;

import java.util.ArrayList;

//task model class that holds Name of task and supports task completion (updating next child)
public class Task {
    private String taskName;
    private Child child;
    private int childIndex;
    private final ArrayList<Child> listOfChildren;   //build new list of children from shared preferences every time activity clicked?

    Task(String taskName, Child child, ArrayList<Child> listOfChildren){
        this.taskName = taskName;
        this.child = child;
        this.listOfChildren = listOfChildren;
        this.childIndex = listOfChildren.indexOf(child);
    }

    Task(String taskName, ArrayList<Child> listOfChildren){
        this.taskName = taskName;
        this.childIndex = 0;
        this.listOfChildren = listOfChildren;
        this.child = listOfChildren.get(childIndex);
    }

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public void setTaskChild(Child child){
        this.child = child;
    }

    public String getTaskName(){
        return taskName;
    }

    public Child getTaskChild(){
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
