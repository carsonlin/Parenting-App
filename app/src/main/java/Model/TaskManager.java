package Model;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;


import java.util.ArrayList;

//TaskManager class that holds a list of tasks. Class object can be saved in SharedPreferences with GSON library
public class TaskManager {
    public static final String TASKS = "TASKS";
    public static final String TASK_MANAGER = "TASK_MANAGER";
    private final ArrayList<Task> tasks;
    private static TaskManager instance;

    private TaskManager(){
        tasks = new ArrayList<>();
    }

    public static TaskManager getInstance(){
        if(instance == null){
            instance = new TaskManager();
        }
        return instance;
    }

    public void updateTasksOnChildDelete(int indexDeleted, int newNumberOfChildren){
        for (Task task : tasks){
            if (newNumberOfChildren == 0){
                task.setNoChild();
            }
            else if (task.getChildIndex() > indexDeleted){
                task.decrementChildIndex();
            }
            else if (task.getChildIndex() == indexDeleted && task.getChildIndex() == newNumberOfChildren){
                task.setChildIndex(0);
            }
        }
    }

    public void setAllTaskIndexZero(){
        for (Task task : tasks){
            task.setChildIndex(0);
        }
    }

    public void deleteAllTasks(){
        tasks.clear();

        TurnHistoryManager turnHistoryManager = TurnHistoryManager.getInstance();
        turnHistoryManager.deleteAllTurns();
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public void removeTask(int index){
        TurnHistoryManager turnHistoryManager = TurnHistoryManager.getInstance();
        turnHistoryManager.deleteTurns(tasks.get(index).getTaskName());
        tasks.remove(index);
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public Task getTask(int index){
        return tasks.get(index);
    }

    public void saveToSharedPreferences(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(TASKS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(TaskManager.getInstance());
        editor.putString(TASK_MANAGER, json);
        editor.apply();
    }

    public void loadFromSharedPreferences(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(TASKS, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TASK_MANAGER, "");

        if (!json.equals("")){
            instance = gson.fromJson(json, TaskManager.class);
        }
        else{
            instance = new TaskManager();
        }
    }

    public boolean hasName(String newTaskName) {
        for (Task task : tasks){
            if (task.getTaskName().equals(newTaskName)){
                return true;
            }
        }
        return false;
    }
}
