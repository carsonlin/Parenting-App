package Model;

import java.util.ArrayList;

public class TaskManager {
    private ArrayList<Task> taskManager;
    private int managerSize;

    TaskManager(){
        taskManager = new ArrayList<>();
        managerSize = 0;
    }
    public void addTask(Task task){
        taskManager.add(task);
        managerSize++;
    }
    public void removeTask(int index){
        taskManager.remove(index);
        managerSize--;
    }
    public Task getTask(int index){
        return taskManager.get(index);
    }

    public int getManagerSize(){
        return taskManager.size();
    }
}
