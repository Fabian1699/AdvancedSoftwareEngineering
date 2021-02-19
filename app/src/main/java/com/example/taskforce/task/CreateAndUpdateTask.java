package com.example.taskforce.task;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.example.taskforce.ui.main.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.taskforce.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateAndUpdateTask extends AppCompatActivity {
    private EditText taskName;
    private EditText taskDescription;
    private Spinner taskFrequency;
    private Button taskSave;
    private CalendarView calendar;
    private LinearLayout dateView;
    private EditText customFrequency;
    private Button addSubTask;
    private ListView subTasks;

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
                save();
                finish();
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

        TaskFactory factory = new TaskFactory();
        List<String> subTaskNames = new ArrayList<>();
        ArrayAdapter<CharSequence> subTaskAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1);
        subTaskAdapter.addAll(subTaskNames);
        subTasks.setAdapter(subTaskAdapter);
        Utility.setListViewHeightBasedOnChildren(subTasks);

        addSubTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subTaskNames.add(taskDescription.getText().toString());
                subTaskAdapter.add(taskDescription.getText().toString());
                taskDescription.setText("");
                subTaskAdapter.notifyDataSetChanged();
                Utility.setListViewHeightBasedOnChildren(subTasks, 3);
            }
        });
    }

    private boolean save(){
        return true;
    }



}
