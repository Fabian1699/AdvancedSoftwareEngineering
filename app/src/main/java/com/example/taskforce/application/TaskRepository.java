package com.example.taskforce.application;

import com.example.taskforce.domain.ITaskRepository;
import com.example.taskforce.domain.task.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskRepository implements ITaskRepository {
    private final ITaskDAO taskObjectDAO;

    public TaskRepository(ITaskDAO taskObjectDAO){
        this.taskObjectDAO = taskObjectDAO;
    }

    @Override
    public List<Task> getAll() {
        return taskObjectDAO.getAllTaskObjects();
    }

    public List<Task> getAllOpenTasks(){
        return taskObjectDAO.getAllTaskObjects().stream()//
                .filter(task -> !task.isFinished())//
                .collect(Collectors.toList());
    }

    public List<Task> getAllFinishedTasks(){
        return taskObjectDAO.getAllTaskObjects().stream()//
                .filter(Task::isFinished)//
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Task> find(UUID taskId) {
        return taskObjectDAO.getTask(taskId);
    }

    @Override
    public void updateTask(Task task) {
        taskObjectDAO.updateTask(task);
    }

    @Override
    public void deleteTask(UUID taskId){
        taskObjectDAO.deleteTaskFromDatabase(taskId);
    }

    @Override
    public void add(Task task) {
        taskObjectDAO.saveTaskToDatabase(task);
    }
}
