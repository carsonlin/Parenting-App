package Model;

public class TaskChild {

    private String taskName;
    private String childName;

    public TaskChild(String taskName, String childName) {
        this.taskName = taskName;
        this.childName = childName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }
}
