package com.example.taskforce.domain.statistics;

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
}
