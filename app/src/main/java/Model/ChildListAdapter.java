package Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ca.cmpt276.chlorinefinalproject.R;

public class ChildListAdapter extends ArrayAdapter<Child> {
    private final Context context;
    private final int resource;

    static class ViewHolder {
        TextView childName;
        ImageView childImage;
    }

    public ChildListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Child> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String name = getItem(position).getName();
        Bitmap image = getItem(position).getImage();

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            holder = new ViewHolder();
            holder.childName = convertView.findViewById(R.id.name);
            holder.childImage = convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.childName.setText(name);
        Glide.with(context).load(image).circleCrop().into(holder.childImage);

        return convertView;
    }
}
