package com.example.taskforce.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TaskFactory {
    private String taskName;
    private Date targetDate;
    private Date finishDate;
    private Frequency frequency;
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
        return new TaskObject(new Task(taskName, targetDate, frequency));
    }

    public TaskObject build(UUID id){
        return new TaskObject(id, new Task(taskName, targetDate, frequency), finishDate, finished, subTasks);
    }


}
