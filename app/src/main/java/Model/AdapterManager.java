package Model;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

// Manages the RecyclerView adapter for the task turn history.
// Holds a reference to a single adapter that has its data set change
public class AdapterManager {

    private RecyclerViewTurnHistory adapter;
    private static AdapterManager instance;
    private final ArrayList<TurnHistory> data = new ArrayList<>();

    private AdapterManager() {
    }

    public static AdapterManager getInstance(){
        if(instance == null){
            instance = new AdapterManager();
        }
        return instance;
    }

    public RecyclerViewTurnHistory getAdapter(){
        return adapter;
    }

    public void setAdapter(ChildManager childManager, Context context){
        adapter = new RecyclerViewTurnHistory(childManager, data, context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateDataSet(ArrayList<TurnHistory> hist){
        this.data.clear();
        Collections.reverse(hist);
        this.data.addAll(hist);
        adapter.notifyDataSetChanged();
    }
}
