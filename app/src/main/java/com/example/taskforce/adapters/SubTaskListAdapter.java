package com.example.taskforce.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.taskforce.R;
import com.example.taskforce.adapters.database.TaskObjectDAO;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.TaskObject;

import java.util.HashMap;
import java.util.Map;

public class SubTaskListAdapter extends BaseAdapter {
    private final TaskObject data;
    private static LayoutInflater inflater = null;
    private Map<String, View> views = new HashMap<>();
    private final TaskObjectDAO taskObjectDAO;

    public SubTaskListAdapter(TaskObjectDAO taskObjectDAO, TaskObject data) {
        this.taskObjectDAO = taskObjectDAO;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.getSubTasks().size();
    }

    @Override
    public Object getItem(int position) {
        return data.getSubTasks().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = views.get((SubTask) getItem(position));
        if(v==null) {
            SubTask subTask = data.getSubTasks().get(position);
            v = inflater.inflate(R.layout.subtask, null);
            TextView name = v.findViewById(R.id.subtaskName);
            CheckBox check = v.findViewById(R.id.subtaskCheck);
            name.setText(subTask.getTaskName());
            check.setChecked(subTask.isFinished());

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                private int pos = position;

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SubTask sub = data.getSubTasks().get(pos);
                    if(isChecked){
                        SubTask subFinished = sub.finish();
                        taskObjectDAO.updateSubTask(data.getId(), subFinished);
                    }else{
                        taskObjectDAO.updateSubTask(data.getId(), new SubTask(sub.getTaskName()));
                    }
                    View listViewParent = ((View) buttonView.getParent().getParent().getParent());
                    listViewParent.callOnClick();
                }
            });
            views.put(subTask.getTaskName(), v);
        }
        return v;
    }
}
