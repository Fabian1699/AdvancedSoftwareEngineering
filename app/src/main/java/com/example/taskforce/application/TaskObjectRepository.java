package com.example.taskforce.application;

import com.example.taskforce.domain.task.ITaskObjectRepository;
import com.example.taskforce.domain.task.TaskObject;

import java.util.List;
import java.util.stream.Collectors;

public class TaskObjectRepository implements ITaskObjectRepository {
    private final ITaskObjectDAO taskObjectDAO;

    public TaskObjectRepository(ITaskObjectDAO taskObjectDAO){
        this.taskObjectDAO = taskObjectDAO;
    }

    @Override
    public List<TaskObject> getAll(boolean onlyOpenTasks) {
        return taskObjectDAO.getAllTaskObjects();
    }

    public List<TaskObject> getAllOpenTasks(){
        return taskObjectDAO.getAllTaskObjects().stream()//
                .filter(task -> !task.isFinished())//
                .collect(Collectors.toList());
    }

    public List<TaskObject> getAllFinishedTasks(){
        return taskObjectDAO.getAllTaskObjects().stream()//
                .filter(TaskObject::isFinished)//
                .collect(Collectors.toList());
    }
}
