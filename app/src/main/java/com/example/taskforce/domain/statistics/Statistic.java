package com.example.taskforce.domain.statistics;

import java.util.Objects;

public class Statistic {
    private final String taskName;
    private final double timeSpentMinutes;
    private final double timePercentage;

    public Statistic(String taskName, double timeSpentMinutes, double timePercentage) {
        this.taskName = taskName;
        this.timeSpentMinutes = timeSpentMinutes;
        this.timePercentage = timePercentage;
    }

    public String getTaskName() {
        return taskName;
    }

    public double getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    public double getTimePercentage() {
        return timePercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statistic)) return false;
        Statistic statistic = (Statistic) o;
        return Double.compare(statistic.timeSpentMinutes, timeSpentMinutes) == 0 &&
                Double.compare(statistic.timePercentage, timePercentage) == 0 &&
                Objects.equals(taskName, statistic.taskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, timeSpentMinutes, timePercentage);
    }
}
