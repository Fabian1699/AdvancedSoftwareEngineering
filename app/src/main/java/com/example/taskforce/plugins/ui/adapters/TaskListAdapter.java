package com.example.taskforce.plugins.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taskforce.R;
import com.example.taskforce.application.TaskRepository;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.domain.task.TaskObject;
import com.example.taskforce.plugins.ui.util.ListViewSizeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TaskListAdapter extends BaseAdapter {
    Context context;
    private static LayoutInflater inflater = null;
    private final TaskRepository repository;
    private final boolean openTasks;

    private Map<Integer, Map<UUID, View>> views = new HashMap<>();

    public TaskListAdapter(Context context, TaskRepository repository, boolean openTasks) {
        this.context = context;
        this.repository = repository;
        this.openTasks=openTasks;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        generateViews(repository, inflater, openTasks);
    }

    private void generateViews(TaskRepository repository, LayoutInflater inflater, boolean openTasks) {
        List<Task> tasks = getTasksToDisplay();

        int count = 0;
        for(Task task:tasks) {

            View v = inflater.inflate(R.layout.task, null);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View parentOfListView = (View) v.getParent().getParent().getParent().getParent();
                    parentOfListView.performClick();
                }
            });

            TextView name = v.findViewById(R.id.taskViewName);
            CheckBox check = v.findViewById(R.id.taskViewCheck);
            ProgressBar progress = v.findViewById(R.id.taskViewProgress);
            ListView lvSubTasks = v.findViewById(R.id.lv_subtasks);
            ImageButton deleteTask = v.findViewById(R.id.deleteTask);

            name.setText(task.getTaskObjectCopy().getTaskBase().getName());
            progress.setProgress((int)(task.progress()*100), true);
            check.setChecked(task.isFinished());

            SubTaskListAdapter adapter = new SubTaskListAdapter(task.getId(), repository, inflater, openTasks);
            lvSubTasks.setAdapter(adapter);
            check.setEnabled(openTasks);

            deleteTask.setOnClickListener(new View.OnClickListener() {
                private UUID taskId = task.getId();
                @Override
                public void onClick(View button) {
                    repository.deleteTask(taskId);
                    notifyDataSetChanged();
                }
            });


            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Investierte Zeit");
                    builder.setView(inflater.inflate(R.layout.time_spend_dialog, null));

                    //TODO take time spent

                    DialogInterface.OnClickListener abortListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    };

                    DialogInterface.OnClickListener finishListener = new DialogInterface.OnClickListener() {
                        UUID taskId = task.getId();

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            View taskLayout =(View) buttonView.getParent().getParent();
                            ListView taskListView = (ListView) taskLayout.getParent();
                            //TODO Optional handling
                            Task taskToFinish = repository.find(taskId).get();
                            taskToFinish.finishTask(10);
                            repository.updateTask(taskToFinish);
                            ((View)taskListView.getParent().getParent().getParent()).callOnClick();
                        }
                    };
                    builder.setNegativeButton("abort", abortListener);
                    builder.setPositiveButton("finish", finishListener);

                    builder.create().show();
                }
            });

            ListViewSizeUtil.setListViewHeightBasedOnChildren(lvSubTasks);
            Map<UUID, View> taskView = new HashMap<>();
            taskView.put(task.getId(), v);

            views.put(count, taskView);
            count++;
        }

    }

    private List<Task> getTasksToDisplay(){
        if(openTasks) {
            return repository.getAllOpenTasks();
        }
        return repository.getAllFinishedTasks();
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object getItem(int position) {
        return "";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return (View) views.get(position).values().stream().findFirst().get();
    }

    @Override
    public void notifyDataSetChanged() {
        if(getTasksToDisplay().size()!=views.size()){
            views.clear();
            generateViews(repository,inflater, openTasks);
            super.notifyDataSetChanged();
            return;
        }

        views.entrySet().stream().forEach(e -> {
            Map.Entry entry = e.getValue().entrySet().stream().findFirst().get();
            View v = (View) entry.getValue();

            Optional<Task> taskOpt = repository.find((UUID)entry.getKey());

            if(taskOpt.isPresent()) {
                Task task = taskOpt.get();
                TextView name = v.findViewById(R.id.taskViewName);
                CheckBox check = v.findViewById(R.id.taskViewCheck);
                ProgressBar progress = v.findViewById(R.id.taskViewProgress);
                ListView lvSubTasks = v.findViewById(R.id.lv_subtasks);

                name.setText(task.getTaskObjectCopy().getTaskBase().getName());
                progress.setProgress((int) (task.progress() * 100), true);
                check.setChecked(task.isFinished());
            }
        });
        super.notifyDataSetChanged();
    }

}
