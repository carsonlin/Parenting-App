package Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import ca.cmpt276.chlorinefinalproject.R;

public class RecyclerViewCoinFlipHistory extends RecyclerView.Adapter<RecyclerViewCoinFlipHistory.MyViewHolder> {

    private final GameManager gameManager;
    private final Context context;

    public RecyclerViewCoinFlipHistory(GameManager gameManager, Context context){
        this.gameManager = gameManager;
        this.context = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView coinFlipchild;
        private final TextView coinFlipdatetime;
        private final ImageView coinFliptrash;
        private final ImageView coinFlipoutcome;

        public MyViewHolder(final View view){
            super(view);
            coinFlipchild = view.findViewById(R.id.coinFlipchild);
            coinFlipdatetime = view.findViewById(R.id.coinFlipdatetime);
            coinFliptrash = view.findViewById(R.id.coinFliptrashHistory);
            coinFlipoutcome = view.findViewById(R.id.coinFlipoutcome);
            coinFliptrash.setOnClickListener(view1 -> removeUpdateManager(view,getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public RecyclerViewCoinFlipHistory.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_flip_history_entity,parent,false);
        return new RecyclerViewCoinFlipHistory.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCoinFlipHistory.MyViewHolder holder, int position) {
        Game game = gameManager.getSavedGamesFromSharedPreferences().get(position);
        ChildPick child = game.getChild();
        int outcome = game.isHead()==child.isHeads()?R.drawable.icons8_checkmark_60:R.drawable.icons8_x_50;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = game.getTime().format(formatter);
        String outcomeText = (child.isHeads()?" Heads ":" Tails ");
        String childText = " Uncofigured child chose "+outcomeText;
        if(!child.getName().isEmpty())
            childText = child.getName()+" chose "+outcomeText;
        holder.coinFlipchild.setText(childText);
        holder.coinFlipdatetime.setText(formattedDateTime);
        Glide.with(this.context).load(outcome).into(holder.coinFlipoutcome);
    }

    public void removeUpdateManager(View view, int index){
        gameManager.removeGamehistory(index);
        this.notifyItemRemoved(index);
        this.notifyItemRangeChanged(index, gameManager.getSavedGamesFromSharedPreferences().size());
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return gameManager.getSavedGamesFromSharedPreferences().size();
    }
}
