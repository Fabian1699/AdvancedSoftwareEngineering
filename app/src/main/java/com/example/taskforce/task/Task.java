package com.example.taskforce.task;

import java.util.Date;
import java.util.Objects;

public class Task {
    private final String name;
    private final Date targetDate;
    private final Frequency frequency;

    public Task(String name, Date targetDate, Frequency frequency) {
        this.name = name;
        this.targetDate = targetDate;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name) &&
                Objects.equals(targetDate, task.targetDate) &&
                frequency == task.frequency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, targetDate, frequency);
    }
}
