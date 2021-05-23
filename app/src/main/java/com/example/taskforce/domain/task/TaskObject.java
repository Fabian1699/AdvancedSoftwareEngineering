package com.example.taskforce.domain.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TaskObject {
    private final UUID id;
    private final Task task;
    private List<SubTask> subTasks;
    private boolean isFinished = false;
    private Date finishDate;
    private int timeSpentMinutes;


    public TaskObject(UUID id, Task task, Date finishDate, boolean isFinished, int timeSpentMinutes, List<SubTask> subTasks){
        this.id = id;
        this.task=task;
        this.finishDate = finishDate;
        this.isFinished = isFinished;
        this.timeSpentMinutes = timeSpentMinutes;
        this.subTasks = subTasks;
    }

    public TaskObject(UUID id, Task task, List<SubTask> subTasks){
        this.id = id;
        this.task=task;
        this.finishDate = task.getTargetDate();
        this.isFinished = false;
        this.timeSpentMinutes = 0;
        this.subTasks = subTasks;
    }

    public TaskObject(Task task){
        this.id=UUID.randomUUID();
        this.task=task;
        this.finishDate = task.getTargetDate();
        this.timeSpentMinutes = 0;
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

    public int getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    public void replaceSubTask(SubTask oldSub, SubTask newSub){
        subTasks.set(subTasks.indexOf(oldSub), newSub);
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void finishTask(){
        this.finishDate = new Date(System.currentTimeMillis());
        this.isFinished = true;
    }

    public boolean isFinished(){
        return isFinished;
    }

    @Override
    public boolean equals(Object o) {
        //TODO
        if (this == o) return true;
        if (!(o instanceof TaskObject)) return false;
        TaskObject that = (TaskObject) o;
        return isFinished == that.isFinished &&
                Objects.equals(id, that.id) &&
                Objects.equals(task, that.task) &&
                Objects.equals(subTasks, that.subTasks);
    }

    @Override
    //TODO
    public int hashCode() {
        return Objects.hash(id, task, isFinished, subTasks);
    }

    public UUID getId() {
        return id;
    }
}
