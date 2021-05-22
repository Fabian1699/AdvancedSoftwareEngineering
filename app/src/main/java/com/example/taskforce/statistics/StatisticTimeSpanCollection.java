package com.example.taskforce.statistics;

import java.util.ArrayList;
import java.util.List;

public class StatisticTimeSpanCollection {
    private List<Statistic> statistics = new ArrayList<>();
    private TimeSpan timeSpan = TimeSpan.DAY;

    public void addStatistic(Statistic stat){
        statistics.add(stat);
    }

    public void setTimeSpan(TimeSpan span){
        timeSpan = span;
    }
}
