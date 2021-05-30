package com.example.taskforce.domain.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Task {
    private TaskObject taskObject;
    private Set<SubTask> subTasks = new HashSet<>();

    public Task(TaskBase taskBase){
        this.taskObject = new TaskObject(taskBase);
    }

    public Task(UUID id, TaskBase taskBase){
        this.taskObject = new TaskObject(id, taskBase);
    }

    public Task(UUID id, TaskBase taskBase, TaskFinish taskFinish){
        this.taskObject = new TaskObject(id, taskBase, taskFinish);
    }

    public boolean isFinished(){
        return taskObject.isFinished();
    }

    public UUID getId(){
        return taskObject.getId();
    }

    public void finishTask(int timeSpentMinutes){
        taskObject.finishTask(timeSpentMinutes);
    }

    public void addSubTask(String name, boolean isFinished){
        if(subTasks.size()<100 && !subTasks.contains(new SubTask(name, true))
                && !subTasks.contains(new SubTask(name, false))){
            this.subTasks.add(new SubTask(name, isFinished));
        }
    }

    public TaskObject getTaskObjectCopy(){
        return new TaskObject(taskObject.getId(),taskObject.getTaskBase(), taskObject.getTaskFinish());
    }

    public Set<SubTask> getSubTasks(){
        return Collections.unmodifiableSet(this.subTasks);
    }

    public double progress(){
        return subTasks.size()>0 && subTasks.stream().filter(SubTask::isFinished).count()>0?
                subTasks.stream().filter(SubTask::isFinished).count()/(double)subTasks.size()
                : 0;
    }

    public void finishSubTask(String name){
        SubTask subTaskToFinish = new SubTask(name, false);
        SubTask subTaskFinished = new SubTask(name, true);
        if(subTasks.contains(subTaskToFinish)){
            subTasks.remove(subTaskToFinish);
            subTasks.add(new SubTask(subTaskToFinish.getTaskName(), true));
        }else if(subTasks.contains(subTaskFinished)){
            subTasks.remove(subTaskToFinish);
            subTasks.add(new SubTask(subTaskToFinish.getTaskName(), false));
        }
    }
}
