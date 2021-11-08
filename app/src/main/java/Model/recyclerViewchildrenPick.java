package Model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import ca.cmpt276.chlorinefinalproject.CoinFlipActivity;
import ca.cmpt276.chlorinefinalproject.R;

public class recyclerViewchildrenPick extends RecyclerView.Adapter<recyclerViewchildrenPick.MyViewHolder> {

    private ArrayList<String> children;
    private Context context;
    public recyclerViewchildrenPick(ArrayList<String> children,Context context){
        this.children = children;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nametext;
        private Button heads;
        private Button tails;
        private LinearLayout choiceHolder;
        private Boolean expaned = false;

        public MyViewHolder(final View view){
            super(view);
            nametext = (TextView) view.findViewById(R.id.childName);
            heads = (Button) view.findViewById(R.id.head);
            tails = (Button) view.findViewById(R.id.tail);
            choiceHolder = (LinearLayout) view.findViewById(R.id.childrenChoiceslinearLayout);
            heads.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goTotoss(children.get(getAdapterPosition()),"head");
                }
            });
            tails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goTotoss(children.get(getAdapterPosition()),"head");
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int visibility = View.VISIBLE;
                    ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(), (int) convertDpToPx(context, 180));
                    if (expaned) {
                        anim = ValueAnimator.ofInt(view.getMeasuredHeight(), (int) convertDpToPx(context, 90));
                        visibility = View.GONE;
                    }
                    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            int val = (Integer) valueAnimator.getAnimatedValue();
                            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                            layoutParams.height = val;
                            view.setLayoutParams(layoutParams);
                        }

                    });
                    int finalVisibility = visibility;
                    anim.addListener(new AnimatorListenerAdapter()
                    {
                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            expaned = !expaned;
                            choiceHolder.setVisibility(finalVisibility);
                        }
                    });
                    anim.setDuration(1500);
                    anim.start();

                    System.out.println("clicked : " + getAdapterPosition());


                }
            });


        }


    }


    @NonNull
    @Override
    public recyclerViewchildrenPick.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_children_pick_toss,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewchildrenPick.MyViewHolder holder, int position) {
        holder.nametext.setText(children.get(position));
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

    public void goTotoss(String child,String bet){

        Intent intent = new Intent(this.context, CoinFlipActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("child", child);
        intent.putExtra("bet",bet);
        this.context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return children.size();
    }
}

