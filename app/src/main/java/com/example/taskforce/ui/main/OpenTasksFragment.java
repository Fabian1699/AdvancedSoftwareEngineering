package com.example.taskforce.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.taskforce.R;
import com.example.taskforce.database.TaskObjectDAO;
import com.example.taskforce.task.TaskObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class OpenTasksFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<TaskObject> data = new ArrayList<>();

    private PageViewModel pageViewModel;
    private TaskListAdapter adapter;

    public static OpenTasksFragment newInstance(int index) {
        OpenTasksFragment fragment = new OpenTasksFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        data.addAll(getTasksToShow());

        adapter = new TaskListAdapter(getContext(), data);
        ListView lvDailyTasks = root.findViewById(R.id.taskListDaily);
        lvDailyTasks.setAdapter(adapter);

        View test123 = lvDailyTasks.getRootView();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTaskListView(adapter);
                ListViewSizeUtil.setListViewHeightBasedOnChildren(lvDailyTasks);
            }
        });

        ListViewSizeUtil.setListViewHeightBasedOnChildren(lvDailyTasks);

        return root;
    }

    private List<TaskObject> getTasksToShow(){
        if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
            return TaskObjectDAO.getAllOpenTasks(getContext());
        }
        return TaskObjectDAO.getAllFinishedTasks(getContext());
    }

    private void updateTaskListView(TaskListAdapter adapter) {
        List<TaskObject> tasks = getTasksToShow();
        if(!data.equals(tasks)){
            data.clear();
            data.addAll(tasks);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onResume() {
        updateTaskListView(adapter);
        super.onResume();
    }

}