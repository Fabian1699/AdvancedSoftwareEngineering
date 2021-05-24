package com.example.taskforce.domain.task;

import java.util.Date;
import java.util.Objects;

public final class TaskBase {
    private final String name;
    private final Date targetDate;
    private final Frequency frequency;

    public TaskBase(String name, Date targetDate, Frequency frequency) {
        this.name = name;
        this.targetDate = targetDate;
        this.frequency = frequency;
    }

    public final String getName() {
        return name;
    }

    public final Date getTargetDate() {
        return targetDate;
    }

    public final Frequency getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskBase taskBase = (TaskBase) o;
        return name.equals(taskBase.name) &&
                Objects.equals(targetDate, taskBase.targetDate) &&
                frequency == taskBase.frequency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, targetDate, frequency);
    }
}
