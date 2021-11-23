package Model;

//simple class of two Strings that is used in the ListView adapter
public class TaskChild {

    private final String taskName;
    private final String childName;

    public TaskChild(String taskName, String childName) {
        this.taskName = taskName;
        this.childName = childName;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getChildName() {
        return childName;
    }
}
