package com.example.taskforce.domain.statistics;

import java.util.ArrayList;
import java.util.List;

//Bisher nicht verwendet, dient zur Zuordnung der Statistiken nach der Zeitspanne: Tag, Woche und Monat
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
