package com.example.taskforce.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskFactory {
    private String taskName;
    private Date targetDate;
    private Frequency frequency;

    private List<SubTask> subTasks = new ArrayList<>();

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public TaskObject build(){
        return new TaskObject(UUID.randomUUID(), new Task(taskName, targetDate, frequency), subTasks);
    }

    public TaskObject build(UUID id){
        return new TaskObject(id, new Task(taskName, targetDate, frequency), subTasks);
    }


}
