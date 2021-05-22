package com.example.taskforce.statistics;

import com.example.taskforce.database.TaskObjectDAO;
import com.example.taskforce.task.TaskObject;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticGenerator {
    TaskObjectDAO taskObjectDAO;

    public StatisticGenerator(TaskObjectDAO dao){
        taskObjectDAO = dao;
    }

    public StatisticTimeSpanCollection getDalyStatistics(){

        List<TaskObject> finishedTasks = taskObjectDAO.getAllFinishedTasks().stream()//
            .sorted(new TaskComapartor())//
            .collect(Collectors.toList());







        return new StatisticTimeSpanCollection();
    }

    private class TaskComapartor implements Comparator<TaskObject>{

        @Override
        public int compare(TaskObject o1, TaskObject o2) {
            return o1.getFinishDate().compareTo(o2.getFinishDate());
        }
    }
}
