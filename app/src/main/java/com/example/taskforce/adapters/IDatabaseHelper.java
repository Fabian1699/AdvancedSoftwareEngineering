package com.example.taskforce.adapters;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.taskforce.domain.task.SubTask;

import java.util.List;
import java.util.Map;

public interface IDatabaseHelper {
    public boolean addTask(String taskObjectId, String taskName, String targetDate, String finishDate, String timeSpentMinutes, String frequency, String isFinished);

    public boolean addSubTask(String taskObjectId, SubTask sub);

    public List<Map<TaskObjectValues, String>> getTasks();

    public List<Map<TaskObjectValues, String>> getSubTasks(String id);

    public void updateTaskFinished(String taskId, String finishDate, String timeSpentMinutes, String isFinished);

    public void updateSubTaskFinished(String taskId, String name, boolean isFinished);

    public void deleteTask(String id);
}
