package Model;

import java.util.ArrayList;

//TaskManager class that holds a list of tasks (may not be needed)
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
