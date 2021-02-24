package com.example.taskforce.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taskforce.R;
import com.example.taskforce.database.DatabaseHelper;
import com.example.taskforce.database.TaskObjectDAO;
import com.example.taskforce.task.SubTask;
import com.example.taskforce.task.TaskObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TaskListAdapter extends BaseAdapter {
    Context context;
    List<TaskObject> data;
    private static LayoutInflater inflater = null;

    private Map<UUID, View> views = new HashMap<>();

    public TaskListAdapter(Context context, List<TaskObject> data) {
        this.context = context;
        this.data = data;
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
        TaskObject taskObj = data.get(position);
        View v = views.get(taskObj.getId());

        if(v==null) {
            v = inflater.inflate(R.layout.task, null);
            TextView name = v.findViewById(R.id.taskViewName);
            CheckBox check = v.findViewById(R.id.taskViewCheck);
            ProgressBar progress = v.findViewById(R.id.taskViewProgress);
            ListView lvSubTasks = v.findViewById(R.id.lv_subtasks);
            name.setText(taskObj.getTask().getName());

            progress.setProgress(calcProgressForTask(taskObj), true);

            SubTaskListAdapter adapter = new SubTaskListAdapter(context, data.get(position));
            lvSubTasks.setAdapter(adapter);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //adapter.notifyDataSetChanged();
                    View parentOfListView = (View) v.getParent().getParent().getParent().getParent();
                    parentOfListView.performClick();
                }
            });

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Investierte Zeit");
                    builder.setView(inflater.inflate(R.layout.time_spend_dialog, null));
                    DialogInterface.OnClickListener abortListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    };

                    DialogInterface.OnClickListener finishListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            View taskLayout =(View) buttonView.getParent().getParent();
                            ListView taskListView = (ListView) taskLayout.getParent();
                            TaskObject obj = data.get(taskListView.getPositionForView(taskLayout));
                            obj.finishTask();
                            TaskObjectDAO.updateTask(context, obj);
                            TaskObjectDAO.deleteTaskFromDatabase(context, obj.getId());
                            ((View)taskListView.getParent().getParent().getParent()).callOnClick();
                        }
                    };
                    builder.setNegativeButton("abort", abortListener);
                    builder.setPositiveButton("finish", finishListener);

                    builder.create().show();
                }
            });

            Utility.setListViewHeightBasedOnChildren(lvSubTasks);
            views.put(taskObj.getId(), v);
        }
        return v;
    }

    private int calcProgressForTask(TaskObject taskObj){
        if(taskObj.getSubTasks().size()>0) {
            int finishedSubTasks = (int) taskObj.getSubTasks().stream().filter(SubTask::isFinished).count();
            return finishedSubTasks*100 / taskObj.getSubTasks().size();
        }
        return 0;
    }


    @Override
    public void notifyDataSetChanged() {

        if(views.size()!=data.size()){
            super.notifyDataSetChanged();
            return;
        }

        for(int i=0; i<data.size(); i++){
            TaskObject taskObj = data.get(i);
            View v = views.get(taskObj.getId());
            if(views ==null){
                v = getView(i,null, null);
            }else{
                TextView name = v.findViewById(R.id.taskViewName);
                CheckBox check = v.findViewById(R.id.taskViewCheck);
                ProgressBar progress = v.findViewById(R.id.taskViewProgress);
                ListView lvSubTasks = v.findViewById(R.id.lv_subtasks);

                //lvSubTasks.setAdapter(new SubTaskListAdapter(context, taskObj));
                name.setText(taskObj.getTask().getName());
                progress.setProgress(calcProgressForTask(taskObj), true);
            }
        }
    }
}
