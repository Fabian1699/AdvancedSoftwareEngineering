package com.example.taskforce.domain.task;

import java.util.Objects;
import java.util.UUID;

public class SubTask {
    private final String taskName;
    private final boolean finished;

    public SubTask(String taskName){
        this.taskName=taskName;
        this.finished=false;
    }

    public SubTask(String taskName, boolean finished){
        this.taskName=taskName;
        this.finished=finished;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isFinished(){
        return finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        SubTask subTask = (SubTask) o;
        return finished == subTask.finished &&
                taskName.equals(subTask.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, finished);
    }
}
