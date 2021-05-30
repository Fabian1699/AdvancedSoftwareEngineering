package com.example.taskforce.application;

import com.example.taskforce.domain.statistics.Statistic;
import com.example.taskforce.domain.ITaskRepository;
import com.example.taskforce.domain.task.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticService {
    ITaskRepository taskObjectRepo;

    public StatisticService(ITaskRepository taskObjectRepo){
        this.taskObjectRepo = taskObjectRepo;
    }

    public List<Statistic> getDalyStatistics(){

        List<Task> finishedTasks = taskObjectRepo.getAllFinishedTasks().stream()//
            .sorted(new TaskComapartor())//
            .collect(Collectors.toList());

        List<Statistic> statistics = new ArrayList<>();

        return statistics;
    }

    private class TaskComapartor implements Comparator<Task>{

        @Override
        public int compare(Task o1, Task o2) {
            return o1.getTaskObjectCopy().getTaskFinish().getFinishDate()
                    .compareTo(o2.getTaskObjectCopy().getTaskFinish().getFinishDate());
        }
    }
}
