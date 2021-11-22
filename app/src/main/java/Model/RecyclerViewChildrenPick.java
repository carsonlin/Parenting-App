package Model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.chlorinefinalproject.CoinFlipActivity;
import ca.cmpt276.chlorinefinalproject.R;

// Recycler view adapter to list children
public class RecyclerViewChildrenPick extends RecyclerView.Adapter<RecyclerViewChildrenPick.MyViewHolder> {
    public static final int ANIMATION_DURATION = 1500;
    private ArrayList<String> listOfChildren;
    private final Activity activity;
    private final GameManager gameManager;
    private final ChildManager childManager;

    public RecyclerViewChildrenPick(boolean isOverride, GameManager gameManager, ChildManager childManager, Activity activity){
        this.activity = activity;
        this.gameManager = gameManager;
        this.childManager = childManager;

        if (isOverride) {
            listOfChildren = gameManager.getQueueOfNewChildrenList();
        } else {
            listOfChildren = gameManager.getNextChildrenToPick();

            ArrayList<String> children = (ArrayList<String>) childManager.getChildNameSharedPreferences(activity);
            if (listOfChildren.isEmpty() && !(children.isEmpty())) {
                this.listOfChildren = children;
            }
        }
    }

    public ArrayList<String> getListOfChildren(){
        return listOfChildren;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView nameText;
        private final LinearLayout choiceHolder;
        private Boolean isExpanded = false;
        private ImageView childProfilePic;

        public MyViewHolder(final View view){
            super(view);
            nameText = view.findViewById(R.id.taskNameTextView);
            choiceHolder = view.findViewById(R.id.childrenChoiceslinearLayout);
            childProfilePic = view.findViewById(R.id.childProfilePic);
            Button headButton = view.findViewById(R.id.head);
            Button tailsButton = view.findViewById(R.id.tail);
            headButton.setOnClickListener(view1 -> goToToss(listOfChildren.get(getAdapterPosition()),
                    activity.getString(R.string.text_heads)));
            tailsButton.setOnClickListener(view12 -> goToToss(listOfChildren.get(getAdapterPosition()),
                    activity.getString(R.string.text_tails)));

            view.setOnClickListener(v -> {
                int visibility = View.VISIBLE;
                ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(), (int) convertDpToPx(activity, 180));

                if (isExpanded) {
                    anim = ValueAnimator.ofInt(view.getMeasuredHeight(), (int) convertDpToPx(activity, 90));
                    visibility = View.GONE;
                }

                anim.addUpdateListener(valueAnimator -> {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = val;
                    view.setLayoutParams(layoutParams);
                });

                int finalVisibility = visibility;

                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        isExpanded = !isExpanded;
                        choiceHolder.setVisibility(finalVisibility);
                    }
                });
                anim.setDuration(ANIMATION_DURATION);
                anim.start();
            });
        }
    }

    public void goToToss(String child, String bet){
        Intent intent = new Intent(this.activity, CoinFlipActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("child", child);
        intent.putExtra("bet", bet);
        this.activity.startActivity(intent);
        this.activity.finish();
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    @NonNull
    @Override
    public RecyclerViewChildrenPick.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_children_pick_toss,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewChildrenPick.MyViewHolder holder, int position) {
         holder.nameText.setText(listOfChildren.get(position));
         int index = this.gameManager.getIndexOfChildFromList(listOfChildren.get(position));
         List<String> pathList = childManager.getFilePathSharedPreferences(this.activity.getApplicationContext());
         if (index>-1)
            Glide.with(this.activity.getApplicationContext()).load(pathList.get(index)).circleCrop().into(holder.childProfilePic);

    }

    @Override
    public int getItemCount() {
        return listOfChildren.size();
    }
}

