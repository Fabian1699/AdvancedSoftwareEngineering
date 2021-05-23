package com.example.taskforce.domain.statistics;

import java.util.ArrayList;
import java.util.List;

public class StatisticTimeSpanCollection {
    private List<Statistic> statistics = new ArrayList<>();
    private TimeSpan timeSpan = TimeSpan.DAY;

    public void addStatistic(Statistic stat){
        this.statistics.add(stat);
    }

    public void setTimeSpan(TimeSpan span){
        this.timeSpan = span;
    }
}
