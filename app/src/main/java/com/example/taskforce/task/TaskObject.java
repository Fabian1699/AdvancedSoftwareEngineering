package com.example.taskforce.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TaskObject {
    private final UUID id;
    private final Task task;
    private boolean isFinished = false;
    private List<SubTask> subTasks;

    public TaskObject(UUID id, Task task, List<SubTask> subTasks){
        this.id = id;
        this.task=task;
        this.subTasks = subTasks;
    }
    public TaskObject(UUID id, Task task, boolean isFinished, List<SubTask> subTasks){
        this.id = id;
        this.task=task;
        this.isFinished = isFinished;
        this.subTasks = subTasks;
    }

    public TaskObject(Task task){
        this.id=UUID.randomUUID();
        this.task=task;
        this.subTasks = new ArrayList<>();
    }

    public void addSubTask(SubTask subTask){
        this.subTasks.add(subTask);
    }


    public Task getTask() {
        return task;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void replaceSubTask(SubTask oldSub, SubTask newSub){
        subTasks.set(subTasks.indexOf(oldSub), newSub);
    }

    public void finishTask(){
        this.isFinished = true;
    }

    public boolean isFinished(){
        return isFinished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskObject)) return false;
        TaskObject that = (TaskObject) o;
        return isFinished == that.isFinished &&
                Objects.equals(id, that.id) &&
                Objects.equals(task, that.task) &&
                Objects.equals(subTasks, that.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, isFinished, subTasks);
    }

    public UUID getId() {
        return id;
    }
}
