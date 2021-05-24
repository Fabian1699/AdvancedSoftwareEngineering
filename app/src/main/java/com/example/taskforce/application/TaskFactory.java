package com.example.taskforce.application;

import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.TaskBase;
import com.example.taskforce.domain.task.TaskObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskFactory {
    private String taskName;
    private Date targetDate;
    private Date finishDate;
    private Frequency frequency;
    private int timeSpentMinutes;
    private boolean finished = false;

    private List<SubTask> subTasks = new ArrayList<>();

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public void setTimeSpentMinutes(int timeSpentMinutes) {
        this.timeSpentMinutes = timeSpentMinutes;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void setFinished(boolean finished){
        this.finished = finished;
    }

    public TaskObject build(){
        return new TaskObject(new TaskBase(taskName, targetDate, frequency));
    }

    public TaskObject build(UUID id){
        return new TaskObject(id, new TaskBase(taskName, targetDate, frequency), finishDate, finished, timeSpentMinutes, subTasks);
    }


}
