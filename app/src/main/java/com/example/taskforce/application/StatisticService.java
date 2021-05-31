package com.example.taskforce.application;

import com.example.taskforce.domain.statistics.Statistic;
import com.example.taskforce.domain.ITaskRepository;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.domain.task.TaskFinish;
import com.example.taskforce.domain.task.TaskObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticService {
    ITaskRepository taskObjectRepo;

    public StatisticService(ITaskRepository taskObjectRepo){
        this.taskObjectRepo = taskObjectRepo;
    }

    public List<Statistic> getDalyStatistics(){

        List<Task> finishedTasks = taskObjectRepo.getAllFinishedTasks();

        int totalTime = finishedTasks.stream()
                .map(Task::getTaskObjectCopy)
                .map(TaskObject::getTaskFinish)
                .mapToInt(TaskFinish::getTimeSpentMinutes)
                .sum();

        Map<String, Statistic> statistics = new HashMap<>();
        for(Task task: finishedTasks){
            String taskName = task.getTaskObjectCopy().getTaskBase().getName();
            int timeSpentMin = task.getTaskObjectCopy().getTaskFinish().getTimeSpentMinutes();
            Statistic stat = statistics.get(taskName);

            if(stat == null){
                statistics.put(taskName, new Statistic(taskName, timeSpentMin, getPercentage(totalTime, timeSpentMin)));
            }else{
                int accumulatedTimeSpent = stat.getTimeSpentMinutes()+timeSpentMin;
                statistics.replace(taskName, new Statistic(
                        stat.getTaskName(),
                        accumulatedTimeSpent,
                        getPercentage(totalTime,accumulatedTimeSpent)));
            }
        }

        return statistics.values().stream()
                .sorted(new StatisticComparator())
                .collect(Collectors.toList());
    }

    private double getPercentage(int total, int time){
        return Double.valueOf(time)/Double.valueOf(total);
    }

    private class StatisticComparator implements Comparator<Statistic>{

        @Override
        public int compare(Statistic s1, Statistic s2) {
            return Double.compare(s2.getTimePercentage(), s1.getTimePercentage());
        }
    }
}
