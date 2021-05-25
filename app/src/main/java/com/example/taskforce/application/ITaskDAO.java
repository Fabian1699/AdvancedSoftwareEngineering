package com.example.taskforce.application;

import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.domain.task.TaskObject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskDAO {
    public List<Task> getAllTaskObjects();

    public Optional<Task> getTask(UUID id);

    public boolean saveTaskToDatabase(Task task);

    public void updateTask(Task task);

    public void deleteTaskFromDatabase(UUID id);
}
