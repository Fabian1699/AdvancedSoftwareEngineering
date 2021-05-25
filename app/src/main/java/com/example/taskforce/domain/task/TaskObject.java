package com.example.taskforce.domain.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TaskObject {
    private final UUID id;
    private final TaskBase taskBase;
    private TaskFinish taskFinish;


    public TaskObject(UUID id, TaskBase taskBase, TaskFinish taskFinish){
        this.id = id;
        this.taskBase = taskBase;
        this.taskFinish = taskFinish;
    }

    public TaskObject(UUID id, TaskBase taskBase){
        this.id = id;
        this.taskBase = taskBase;
        this.taskFinish = new TaskFinish(false,taskBase.getTargetDate(), 0);
    }

    public TaskObject(TaskBase taskBase){
        this.id=UUID.randomUUID();
        this.taskBase = taskBase;
        this.taskFinish = new TaskFinish(false,taskBase.getTargetDate(), 0);
    }

    public UUID getId() {
        return id;
    }

    public TaskBase getTaskBase() {
        return new TaskBase(taskBase.getName(), taskBase.getTargetDate(), taskBase.getFrequency());
    }

    public TaskFinish getTaskFinish() {
        return new TaskFinish(taskFinish.isFinished(), taskFinish.getFinishDate(), taskFinish.getTimeSpentMinutes());
    }

    public void finishTask(int timeSpentMinutes){
        if(!taskFinish.isFinished()) {
            this.taskFinish = new TaskFinish(true, new Date(System.currentTimeMillis()),
                    timeSpentMinutes<0? 0:timeSpentMinutes);
        }
    }

    public boolean isFinished(){
        return taskFinish.isFinished();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskObject)) return false;
        TaskObject that = (TaskObject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
