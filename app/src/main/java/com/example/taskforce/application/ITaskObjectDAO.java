package com.example.taskforce.application;

import com.example.taskforce.adapters.database.TaskObjectValues;
import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.domain.task.TaskObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public interface ITaskObjectDAO {
    public List<TaskObject> getAllTaskObjects();

    public boolean saveTaskToDatabase(TaskObject taskObject);

    public void updateTask(TaskObject taskObject);

    public void updateSubTask(UUID taskId, SubTask subTask);

    public void deleteTaskFromDatabase(UUID id);
}
