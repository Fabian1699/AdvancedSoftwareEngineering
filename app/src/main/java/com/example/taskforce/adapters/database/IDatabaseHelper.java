package com.example.taskforce.adapters.database;

import java.util.List;
import java.util.Map;

public interface IDatabaseHelper {
    public boolean addTask(String taskObjectId, String taskName, String targetDate, String finishDate, String timeSpentMinutes, String frequency, String isFinished);

    public boolean addSubTask(String taskObjectId, String subTaskName, String isSubTaskFinished);

    public List<Map<TaskValues, String>> getTasks();

    public List<Map<TaskValues, String>> getSubTasks(String id);

    public Map<TaskValues, String> getTask(String id);

    public void updateTaskFinished(String taskId, String finishDate, String timeSpentMinutes, String isFinished);

    public void updateSubTaskFinished(String taskId, String name, boolean isFinished);

    public void deleteTask(String id);
}
