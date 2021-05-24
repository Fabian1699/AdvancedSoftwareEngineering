package com.example.taskforce.application;

import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.TaskObject;

import java.util.List;
import java.util.UUID;

public interface ITaskObjectDAO {
    public List<TaskObject> getAllTaskObjects();

    public boolean saveTaskToDatabase(TaskObject taskObject);

    public void updateTask(TaskObject taskObject);

    public void updateSubTask(UUID taskId, SubTask subTask);

    public void deleteTaskFromDatabase(UUID id);
}
