package Model;

//task model class that holds Name of task and supports task completion (updating next child)
public class Task {
    public static final int NO_CHILD = -1;
    private String taskName;
    private int childIndex;

    public Task(String taskName){
        this.taskName = taskName;

        ChildManager childManager = ChildManager.getInstance();

        if (childManager.hasChildren()){
            this.childIndex = 0;
        }
        else {
            setNoChild();
        }
    }

    public Task(String taskName, int childIndex){
        this.taskName = taskName;
        this.childIndex = childIndex;
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

    public void setNoChild(){
        setChildIndex(NO_CHILD);
    }

    public boolean hasChild(){
        return childIndex != NO_CHILD;
    }

    public void completeTask(){
        ChildManager childManager = ChildManager.getInstance();
        int numberOfChildren = childManager.getNumberOfChildren();

        if (numberOfChildren != 0){
            childIndex = (childIndex + 1) % numberOfChildren;
        }
    }
}
