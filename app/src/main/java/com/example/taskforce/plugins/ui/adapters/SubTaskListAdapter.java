package com.example.taskforce.plugins.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.taskforce.R;
import com.example.taskforce.adapters.database.TaskDAO;
import com.example.taskforce.application.TaskRepository;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SubTaskListAdapter extends BaseAdapter {
    private final UUID taskId;
    private final boolean openTasks;
    private static LayoutInflater inflater = null;
    private Map<Integer, Map<String, View>> views = new HashMap<>();
    private final TaskRepository repository;

    public SubTaskListAdapter(UUID taskId, TaskRepository repository, LayoutInflater inflater, boolean openTasks) {
        this.repository = repository;
        this.taskId = taskId;
        this.inflater = inflater;
        this.openTasks= openTasks;
        generateViews();
    }

    private void generateViews() {
        int count =0;
        Optional<Task> task  = repository.find(taskId);
        if(task.isPresent()){
            for(SubTask sub:task.get().getSubTasks()){
                View v = inflater.inflate(R.layout.subtask, null);
                TextView name = v.findViewById(R.id.subtaskName);
                CheckBox check = v.findViewById(R.id.subtaskCheck);
                name.setText(sub.getTaskName());
                check.setChecked(sub.isFinished());

                if(openTasks) {
                    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        private String subTaskName = sub.getTaskName();

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            Optional<Task> taskOpt = repository.find(taskId);
                            if (taskOpt.isPresent()) {
                                Task task = taskOpt.get();
                                task.finishSubTask(subTaskName);
                                repository.updateTask(task);
                            }

                            View listViewParent = ((View) buttonView.getParent().getParent().getParent());
                            listViewParent.callOnClick();
                        }
                    });
                }else{
                    check.setEnabled(false);
                }
                Map<String, View> subTaskView = new HashMap<>();
                subTaskView.put(sub.getTaskName(), v);

                views.put(count,subTaskView);
                count++;
            }
        }
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object getItem(int position) {
        return views.get(position).values().stream().findFirst().get();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return (View) getItem(position);
    }

    @Override
    public void notifyDataSetChanged() {
        views.clear();
        generateViews();
        super.notifyDataSetChanged();
    }
}
