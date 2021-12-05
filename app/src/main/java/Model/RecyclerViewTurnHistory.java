package Model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import ca.cmpt276.chlorinefinalproject.R;

// Recycler view adapter to list the whose turn history
public class RecyclerViewTurnHistory extends RecyclerView.Adapter<RecyclerViewTurnHistory.MyViewHolder> {
    private final ChildManager childManager;
    private final ArrayList<TurnHistory> taskHist;
    private final Context context;

    public RecyclerViewTurnHistory(ChildManager childManager, ArrayList<TurnHistory> taskHist, Context context){
        this.childManager = childManager;
        this.taskHist = taskHist;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView childName;
        private final TextView turnDateTime;
        private final ImageView childProfilePic;

        public MyViewHolder(final View view){
            super(view);
            childName = view.findViewById(R.id.whoseTurnChild);
            turnDateTime = view.findViewById(R.id.turnDatetime);
            childProfilePic = view.findViewById(R.id.whoseTurnChildImage);
        }
    }

    @NonNull
    @Override
    public RecyclerViewTurnHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.whose_turn_history_entity,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTurnHistory.MyViewHolder holder, int position) {
        TurnHistory currentItem = taskHist.get(position);

        if (currentItem.hasNoChild()){
            holder.childName.setText(R.string.delete_child);
            holder.turnDateTime.setText(currentItem.getDatetime());
        }
        else{
            holder.childName.setText(childManager.getChild(currentItem.getChildIndex()).getName());
            holder.turnDateTime.setText(currentItem.getDatetime());
            Glide.with(this.context).load(childManager.getChild(currentItem.getChildIndex()).getImage()).circleCrop().into(holder.childProfilePic);
        }
    }

    @Override
    public int getItemCount() {
        return taskHist.size();
    }

}