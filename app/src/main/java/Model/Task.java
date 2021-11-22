package Model;

//task model class that holds Name of task and supports task completion (updating next child)
public class Task {
    private String taskName;
    private int childIndex;

    public Task(String taskName){
        this.taskName = taskName;

        ChildManager childManager = ChildManager.getInstance();

        if (childManager.hasChildren()){
            this.childIndex = 0;
        }
        else {
            this.childIndex = -1;
        }
    }

    public void setTaskName(String taskName){
        this.taskName = taskName;
    }

    public String getTaskName(){
        return taskName;
    }

    public int getChildIndex(){
        return childIndex;
    }

    public void decrementChildIndex(){
        childIndex--;
    }

    public void setChildIndex(int index){
        childIndex = index;
    }

    public void completeTask(){
        ChildManager childManager = ChildManager.getInstance();
        int numberOfChildren = childManager.getNumberOfChildren();

        if (numberOfChildren != 0){
            childIndex = (childIndex + 1) % numberOfChildren;
        }
    }
}
