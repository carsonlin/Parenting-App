package Model;

import java.util.ArrayList;

//TaskManager class that holds a list of tasks (may not be needed)
public class TaskManager {
    private final ArrayList<Task> tasks;
    private int managerSize;

    private static TaskManager instance;

    private TaskManager(){
        tasks = new ArrayList<>();
    }

    //singleton support
    public static TaskManager getInstance(){
        if(instance == null){
            instance = new TaskManager();
        }
        return instance;
    }

    public void updateTasksOnChildDelete(int indexDeleted, int newNumberOfChildren){

        for (Task task : tasks){

            if (newNumberOfChildren == 0){
                task.setChildIndex(-1);
            }
            else if (task.getChildIndex() > indexDeleted){
                task.decrementChildIndex();
            }
            else if (task.getChildIndex() == indexDeleted && task.getChildIndex() == newNumberOfChildren){
                task.setChildIndex(0);
            }
        }
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void removeTask(int index){
        tasks.remove(index);
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public Task getTask(int index){
        return tasks.get(index);
    }

    public int getManagerSize(){
        return tasks.size();
    }
}
