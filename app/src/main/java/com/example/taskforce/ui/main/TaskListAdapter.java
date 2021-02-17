package com.example.taskforce.ui.main;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.example.taskforce.R;
import com.example.taskforce.task.TaskObject;

import java.util.List;

public class TaskListAdapter extends BaseAdapter {
    Context context;
    List<TaskObject> data;
    private static LayoutInflater inflater = null;
    private int layoutId;

    public TaskListAdapter(Context context, int layoutId, List<TaskObject> data) {
        this.context = context;
        this.data = data;
        this.layoutId=layoutId;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.task,null);
        TextView name = v.findViewById(R.id.taskViewName);
        CheckBox check = v.findViewById(R.id.taskViewCheck);
        ProgressBar progress = v.findViewById(R.id.taskViewProgress);
        name.setText(data.get(position).getTask().getName());
        return v;
    }

}
