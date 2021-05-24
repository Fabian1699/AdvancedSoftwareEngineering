package com.example.taskforce.domain.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TaskUnit {
    private final TaskObject taskObject;
    private Set<SubTask> subTasks;

    public TaskUnit(TaskBase taskBase, List<SubTask> subTasks){
        this.taskObject = new TaskObject(taskBase);
        this.subTasks = subTasks.size()<100? subTasks:subTasks.subList(0,99);
    }

    public void finishTask(Date finishDate, int timeSpentMinutes){
        taskObject.finishTask(finishDate, timeSpentMinutes);
    }

    public void addSubTask(String name){
        if(subTasks.size()<100 && !subTasks.contains(new SubTask(name, true))){
            this.subTasks.add(new SubTask(name);)
        }
    }

    public void finishSubTask(String name){
        SubTask subTaskToFinish = new SubTask(name);
        if(subTasks.contains(subTaskToFinish)){
            subTasks.remove(subTaskToFinish);
            subTasks.add(new SubTask(subTaskToFinish.getTaskName(), true));
        }
    }
}
