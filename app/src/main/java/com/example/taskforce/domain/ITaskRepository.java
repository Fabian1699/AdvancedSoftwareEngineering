package com.example.taskforce.domain;

import com.example.taskforce.domain.task.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskRepository {
    public List<Task> getAll();

    public List<Task> getAllOpenTasks();

    public List<Task> getAllFinishedTasks();

    public Optional<Task> find(UUID taskId);

    public void updateTask(Task task);

    public void add(Task task);

}
