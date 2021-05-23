package com.example.taskforce.plugins.ui.viewmodel;

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
import com.example.taskforce.adapters.database.TaskObjectDAO;
import com.example.taskforce.application.TaskObjectRepository;
import com.example.taskforce.domain.task.TaskObject;
import com.example.taskforce.plugins.ui.adapters.TaskListAdapter;
import com.example.taskforce.plugins.database.DatabaseHelper;
import com.example.taskforce.plugins.ui.util.ListViewSizeUtil;

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
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        data.addAll(getTasksToShow());

        adapter = new TaskListAdapter(getContext(),new TaskObjectDAO(new DatabaseHelper(getContext())), data);
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
        TaskObjectRepository repo = new TaskObjectRepository(new TaskObjectDAO(new DatabaseHelper(getContext())));
        if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
            return repo.getAllOpenTasks();
        }
        return repo.getAllFinishedTasks();
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