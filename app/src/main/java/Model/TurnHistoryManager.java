package Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

// manages a list of TurnHistory objects. Singleton
public class TurnHistoryManager {

    private static final String TURNS = "TURN HIST";
    private static final String TURN_MANAGER = "TURN MANAGER";
    private final ArrayList<TurnHistory> turns;
    private static TurnHistoryManager instance;

    private TurnHistoryManager(){
        turns = new ArrayList<>();
    }

    public static TurnHistoryManager getInstance(){
        if(instance == null){
            instance = new TurnHistoryManager();
        }
        return instance;
    }

    public void addTurn(TurnHistory turnHistory){
        turns.add(turnHistory);
    }

    public void updateTurnHistoryOnChildDelete(int indexDeleted, int newNumberOfChildren){
        for (TurnHistory turnHistory : turns){
            if (turnHistory.getChildIndex() == indexDeleted){
                turnHistory.setNoChild();
            }
            else if (newNumberOfChildren == 0){
                turnHistory.setNoChild();
            }
            else if (turnHistory.getChildIndex() > indexDeleted){
                turnHistory.decrementChildIndex();
            }
            else if (turnHistory.getChildIndex() == indexDeleted && turnHistory.getChildIndex() == newNumberOfChildren){
                turnHistory.setChildIndex(0);
            }
        }
    }

    public ArrayList<TurnHistory> getSingleTaskHistory(String taskName){
        ArrayList<TurnHistory> singleTaskHist = new ArrayList<>();

        for (TurnHistory turnHistory : turns){
            if (turnHistory.getTaskName().equals(taskName)){
                singleTaskHist.add(turnHistory);
            }
        }
        return singleTaskHist;
    }

    public void renameTask(String oldName, String newName){
        for (TurnHistory turnHistory : turns){
            if (turnHistory.getTaskName().equals(oldName)){
                turnHistory.setTaskName(newName);
            }
        }
    }

    public void saveToSharedPreferences(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(TURNS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(TurnHistoryManager.getInstance());
        editor.putString(TURN_MANAGER, json);
        editor.apply();
    }

    public void loadFromSharedPreferences(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(TURNS, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(TURN_MANAGER, "");

        if (!json.equals("")){
            instance = gson.fromJson(json, TurnHistoryManager.class);
        }
        else{
            instance = new TurnHistoryManager();
        }
    }

    public void deleteAllTurns() {
        turns.clear();
    }

    public void deleteTurns(String nameOfTask) {
        turns.removeIf(turnHistory -> turnHistory.getTaskName().equals(nameOfTask));
    }
}
