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

import java.time.format.DateTimeFormatter;
import java.util.List;

import ca.cmpt276.chlorinefinalproject.EditChildActivity;
import ca.cmpt276.chlorinefinalproject.R;

public class RecyclerViewCoinFlipHistory extends RecyclerView.Adapter<RecyclerViewCoinFlipHistory.MyViewHolder> {

    private final GameManager gameManager;
    private final Context context;

    public RecyclerViewCoinFlipHistory(GameManager gameManager, Context context){
        this.gameManager = gameManager;
        this.context = context;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView coinFlipChild;
        private final TextView coinFlipDateTime;
        private final ImageView coinFlipOutcome;
        private ImageView childProfilePic;

        public MyViewHolder(final View view){
            super(view);
            coinFlipChild = view.findViewById(R.id.coinFlipchild);
            coinFlipDateTime = view.findViewById(R.id.coinFlipdatetime);
            childProfilePic = view.findViewById(R.id.childProfilePic);
            ImageView coinFlipTrash = view.findViewById(R.id.coinFliptrashHistory);
            coinFlipOutcome = view.findViewById(R.id.coinFlipoutcome);
            coinFlipTrash.setOnClickListener(view1 -> removeUpdateManager(getAdapterPosition()));
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
        int outcome = game.isHead() == child.isHeads()?R.drawable.icons8_checkmark_60:R.drawable.icons8_x_50;
        int index = this.gameManager.getIndexOfChildFromList(child.getName());
        List<String> pathList = EditChildActivity.getFilePathSharedPreferences(this.context);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = game.getTime().format(formatter);

        String outcomeText = (child.isHeads() ?
                context.getString(R.string.text_heads):context.getString(R.string.text_tails));

        String childText;
        if(!child.getName().isEmpty()){
            childText = String.format(context.getString(R.string.coin_flip_history_text), child.getName(), outcomeText);
        }
        else{
            childText = String.format(context.getString(R.string.coin_flip_history_text), context.getString(R.string.user), outcomeText);
        }
        holder.coinFlipChild.setText(childText);
        holder.coinFlipDateTime.setText(formattedDateTime);
        Glide.with(this.context).load(outcome).into(holder.coinFlipOutcome);


        if (index>-1)
            Glide.with(this.context).load(pathList.get(index)).circleCrop().into(holder.childProfilePic);
    }

    public void removeUpdateManager(int index){
        gameManager.removeGameHistory(index);
        this.notifyItemRemoved(index);
        this.notifyItemRangeChanged(index, gameManager.getSavedGamesFromSharedPreferences().size());
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return gameManager.getSavedGamesFromSharedPreferences().size();
    }
}
