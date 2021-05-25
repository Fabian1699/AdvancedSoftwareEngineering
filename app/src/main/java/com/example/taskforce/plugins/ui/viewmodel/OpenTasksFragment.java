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
import com.example.taskforce.adapters.database.TaskDAO;
import com.example.taskforce.application.TaskRepository;
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


        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        TaskDAO taskDAO = new TaskDAO(databaseHelper);
        TaskRepository repository = new TaskRepository(taskDAO);

        adapter = new TaskListAdapter(getContext(), repository, false);
        ListView lvDailyTasks = root.findViewById(R.id.taskListDaily);
        lvDailyTasks.setAdapter(adapter);


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

    private void updateTaskListView(TaskListAdapter adapter) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        updateTaskListView(adapter);
        super.onResume();
    }

}