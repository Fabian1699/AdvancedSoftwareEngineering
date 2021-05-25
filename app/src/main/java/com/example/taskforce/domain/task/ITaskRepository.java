package com.example.taskforce.domain.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITaskRepository {
    public List<Task> getAll(boolean onlyOpenTasks);

    public List<Task> getAllOpenTasks();

    public List<Task> getAllFinishedTasks();

    public Optional<Task> find(UUID taskId);

    public void updateTask(Task task);

    public void add(Task task);

}
