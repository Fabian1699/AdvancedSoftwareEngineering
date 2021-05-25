package com.example.taskforce.application;

import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.domain.task.TaskBase;
import com.example.taskforce.domain.task.TaskFinish;
import com.example.taskforce.domain.task.TaskObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskFactory {
    private UUID id;
    private String taskName;
    private Date targetDate;
    private Date finishDate;
    private Frequency frequency;
    private int timeSpentMinutes = -1;
    private boolean finished = false;
    private Set<SubTask> subTasks = new HashSet<>();

    public void setId(UUID id) {
        this.id = id;
    }

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

    public void setSubTasks(Set<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public void setSubTasksFromString(Set<String> subTasks) {
        this.subTasks = subTasks.stream().map(name -> new SubTask(name, false)).collect(Collectors.toSet());
    }

    public void setFinished(boolean finished){
        this.finished = finished;
    }

    public Optional<Task> build(){
        TaskBase taskBase = null;
        TaskFinish taskFinish = null;

        if(taskName!=null &&  targetDate!=null && frequency !=null){
            taskBase = new TaskBase(taskName, targetDate, frequency);
        }

        if(finished == true && finishDate!=null){
            taskFinish = new TaskFinish(true, finishDate, timeSpentMinutes);
        }

        Task newTask = null;

        if(id != null && taskBase != null && taskFinish != null){
            newTask = new Task(id, taskBase, taskFinish);
        }else if(id != null && taskBase != null ){
            newTask = new Task(id, taskBase);
        }else if(taskBase!=null){
            newTask = new Task(taskBase);
        }
        if(newTask!=null){
            for(SubTask sub:subTasks){
                newTask.addSubTask(sub.getTaskName(), sub.isFinished());
            }
            return Optional.of(newTask);
        }
        return Optional.empty();
    }




}
