package com.example.taskforce.domain.task;

import java.util.Date;
import java.util.Objects;

public class TaskFinish {
    private final boolean isFinished;
    private final Date finishDate;
    private final int timeSpentMinutes;

    public TaskFinish(boolean isFinished, Date finishDate, int timeSpentMinutes) {
        this.isFinished = isFinished;
        this.finishDate = finishDate;
        this.timeSpentMinutes = timeSpentMinutes;
    }

    public final boolean isFinished() {
        return isFinished;
    }

    public final Date getFinishDate() {
        return finishDate;
    }

    public final int getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskFinish)) return false;
        TaskFinish that = (TaskFinish) o;
        return isFinished == that.isFinished &&
                timeSpentMinutes == that.timeSpentMinutes &&
                Objects.equals(finishDate, that.finishDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isFinished, finishDate, timeSpentMinutes);
    }
}
