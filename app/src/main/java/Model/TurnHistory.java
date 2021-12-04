package Model;

public class TurnHistory {

    public String taskName;
    public String datetime;
    public int childIndex;

    public TurnHistory(String taskName, String datetime, int childIndex) {
        this.datetime = datetime;
        this.childIndex = childIndex;
        this.taskName = taskName;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getChildIndex() {
        return childIndex;
    }

    public void setChildIndex(int childIndex) {
        this.childIndex = childIndex;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setNoChild() {
        childIndex = -1;
    }

    public Boolean hasNoChild(){
        return childIndex == -1;
    }

    public void decrementChildIndex() {
        childIndex--;
    }
}
