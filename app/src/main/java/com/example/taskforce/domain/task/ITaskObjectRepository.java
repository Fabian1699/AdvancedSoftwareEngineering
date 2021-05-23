package com.example.taskforce.domain.task;

import com.example.taskforce.domain.task.TaskObject;

import java.util.List;
import java.util.stream.Collectors;

public interface ITaskObjectRepository {
    public List<TaskObject> getAll(boolean onlyOpenTasks);

    public List<TaskObject> getAllOpenTasks();

    public List<TaskObject> getAllFinishedTasks();

}
