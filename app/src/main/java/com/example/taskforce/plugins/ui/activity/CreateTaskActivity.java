package com.example.taskforce.plugins.ui.activity;

import android.os.Bundle;

import com.example.taskforce.adapters.database.TaskDAO;
import com.example.taskforce.application.TaskRepository;
import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.application.TaskFactory;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.plugins.database.DatabaseHelper;
import com.example.taskforce.plugins.ui.util.ListViewSizeUtil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.taskforce.R;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateTaskActivity extends AppCompatActivity {
    private EditText taskName;
    private EditText taskDescription;
    private Spinner taskFrequency;
    private Button taskSave;
    private CalendarView calendar;
    private LinearLayout dateView;
    private EditText customFrequency;
    private Button addSubTask;
    private ListView subTasks;

    Set<String> subTaskNames = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_and_update_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        taskName = findViewById(R.id.taskName);
        taskDescription = findViewById(R.id.taskDescription);
        taskFrequency = findViewById(R.id.taskFrequency);
        taskSave = findViewById(R.id.taskSave);
        dateView = findViewById(R.id.dateView);
        customFrequency = findViewById(R.id.customFrequency);
        calendar = findViewById(R.id.calendar);
        addSubTask = findViewById(R.id.addSubTask);
        subTasks = findViewById(R.id.createdSubTasks);


        calendar.setVisibility(View.GONE);

        customFrequency.setEnabled(false);


        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(Arrays.stream(Frequency.values()).map(Frequency::getKey).collect(Collectors.toList()));
        taskFrequency.setAdapter(adapter);

        taskSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(save()){
                    finish();
                }
            }
        });

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calendar.getVisibility()==View.VISIBLE) {
                    calendar.setVisibility(View.GONE);
                }else{
                    calendar.setVisibility(View.VISIBLE);
                }
            }
        });

        taskFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getItem(position).equals(Frequency.CUSTOM.getKey())){
                    customFrequency.setEnabled(true);
                }else{
                    customFrequency.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> subTaskAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1);
        subTaskAdapter.addAll(subTaskNames);
        subTasks.setAdapter(subTaskAdapter);
        ListViewSizeUtil.setListViewHeightBasedOnChildren(subTasks);

        addSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!taskDescription.getText().toString().equals("")) {
                    subTaskNames.add(taskDescription.getText().toString());
                    subTaskAdapter.add(taskDescription.getText().toString());
                    taskDescription.setText("");
                    subTaskAdapter.notifyDataSetChanged();
                    ListViewSizeUtil.setListViewHeightBasedOnChildren(subTasks, 3);
                }
            }
        });
    }

    private boolean save(){
        TaskFactory fac = new TaskFactory();
        fac.setTaskName(taskName.getText().toString());
        try {
            Frequency freq = Frequency.fromKey((String) taskFrequency.getAdapter().getItem(taskFrequency.getSelectedItemPosition()));
            fac.setFrequency(freq);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        fac.setTargetDate(Date.from(Instant.ofEpochMilli(calendar.getDate())));
        fac.setSubTasksFromString(subTaskNames);
        Optional<Task> task = fac.build();
        if(task.isPresent()){
            new TaskRepository(new TaskDAO(new DatabaseHelper(getBaseContext()))).add(task.get());
            return true;
        }
        return false;
    }

}
