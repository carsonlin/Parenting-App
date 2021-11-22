package Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.chlorinefinalproject.R;

public class TaskListAdapter extends ArrayAdapter<TaskChild> {

    private final Context context;
    private final int resource;

    static class ViewHolder {
        TextView childName;
        TextView taskName;
    }


    public TaskListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TaskChild> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String childName = getItem(position).getChildName();
        String taskName = getItem(position).getTaskName();

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);

            holder = new ViewHolder();
            holder.childName = convertView.findViewById(R.id.childNameList);
            holder.taskName = convertView.findViewById(R.id.taskName);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.childName.setText(childName);
        holder.taskName.setText(taskName);

        return convertView;
    }
}
