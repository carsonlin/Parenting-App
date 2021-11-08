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
    private final RecyclerView recyclerView;
    public RecyclerViewCoinFlipHistory(GameManager gameManager, Context context,RecyclerView recyclerView){
        this.gameManager = gameManager;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView coinFlipchild;
        private final TextView coinFlipdatetime;
        private final TextView coinFliptrash;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_children_pick_toss,parent,false);
        return new RecyclerViewCoinFlipHistory.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCoinFlipHistory.MyViewHolder holder, int position) {
        Game game = gameManager.getSavedGamesFromSharedPreferences().get(position);
        ChildPick child = game.getChild();
        int outcome = game.isHead()?R.drawable.icons8_checkmark_60:R.drawable.icons8_x_50;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(game.getTime().toString(), formatter);
        holder.coinFlipchild.setText(child.getName()+" chose "+(child.isHeads()?" Heads ":" Tails "));
        holder.coinFlipdatetime.setText(dateTime.toString());
        Glide.with(this.context).load(outcome).into(holder.coinFlipoutcome);
    }

    public void removeUpdateManager(View view, int index){
        gameManager.removeGamehistory(index);
        view.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return gameManager.getSavedGamesFromSharedPreferences().size();
    }
}
