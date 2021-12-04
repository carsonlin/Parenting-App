package Model;

import android.util.Log;

import java.util.ArrayList;

public class TurnHistoryManager {

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

    public void removeTurn(int index){
        turns.remove(index);
    }

    public TurnHistory getTurnHistory(int index){
        return turns.get(index);
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
                Log.d("TURN UPDATE", "decrementing index from " + turnHistory.getChildIndex() + " to " + (turnHistory.getChildIndex() - 1));
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
            if (turnHistory.getTaskName().equals(taskName)){ //this is sussy, maybe uniquely identify by datetime?
                singleTaskHist.add(turnHistory);
            }
        }
        return singleTaskHist;
    }

    public int getHistorySizeForTask(String taskName){
        int count = 0;
        for (TurnHistory turnHistory : turns){
            if (turnHistory.getTaskName().equals(taskName)){
                count++;
            }
        }
        return count;
    }


}
