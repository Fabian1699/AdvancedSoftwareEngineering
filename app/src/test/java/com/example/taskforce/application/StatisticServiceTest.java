package com.example.taskforce.application;

import com.example.taskforce.adapters.database.IDatabaseHelper;
import com.example.taskforce.adapters.database.TaskDAO;
import com.example.taskforce.adapters.database.TaskValues;
import com.example.taskforce.domain.statistics.Statistic;
import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.plugins.database.DatabaseHelper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;


import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class StatisticServiceTest {

    @Mock
    private static IDatabaseHelper databaseHelper;

    private static StatisticService statService;

    @BeforeEach
    public void buildObjects(){
        databaseHelper = mock(DatabaseHelper.class);
        TaskDAO dao = new TaskDAO(databaseHelper);
        TaskRepository repo = new TaskRepository(dao);
        statService = new StatisticService(repo);
    }

    @Test
    public void twoFinishedTasksWithEqualNames_SHOULD_sumUpAllTimeSpentMinutesInOneStatistic() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        subTasks.add(getSubTask());
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test1", 20, true));
        finishedTasks.add(getTask("test1", 1, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(1, stats.size());
        assertEquals(21, stats.get(0).getTimeSpentMinutes());
    }

    @Test
    public void twoFinishedTaskNamesAreNotEqual_SHOULD_generateStatisticForEachTask() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        subTasks.add(getSubTask());
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test1", 20, true));
        finishedTasks.add(getTask("test2", 1, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(2, stats.size());
    }

    @Test
    public void finishedTaskNamesAreNotEqual_SHOULD_NOT_sumUpTimeSpentMinutes() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        subTasks.add(getSubTask());
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test1", 20, true));
        finishedTasks.add(getTask("test2", 1, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(2, stats.size());
        assertTrue(stats.get(0).getTimeSpentMinutes()==20 || stats.get(0).getTimeSpentMinutes()==1);
    }

    @Test
    public void multipleFinishedTasksWithDifferentNames_SHOULD_generateStatisticsOrderedByPercentageTimeSpent() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        subTasks.add(getSubTask());
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test1", 20, true));
        finishedTasks.add(getTask("test2", 1, true));
        finishedTasks.add(getTask("test3", 15, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(3, stats.size());
        assertTrue(stats.get(0).getTimePercentage()>=stats.get(1).getTimePercentage());
        assertTrue(stats.get(1).getTimePercentage()>=stats.get(2).getTimePercentage());
    }

    @Test
    public void twoFinishedTasksWithEqualTimeSpentAndDifferentNames_SHOULD_generateStatisticsWithEqualPercentage() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        subTasks.add(getSubTask());
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test1", 20, true));
        finishedTasks.add(getTask("test2", 20, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(2, stats.size());
        assertTrue(stats.get(0).getTimePercentage()==stats.get(1).getTimePercentage());
        assertTrue(stats.get(0).getTimePercentage()==0.5);
    }

    @Test
    public void multipleFinishedTasksWithEqualTimeSpentAndDifferentNames_SHOULD_generateStatisticsWithEqualPercentage() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        subTasks.add(getSubTask());
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test1", 20, true));
        finishedTasks.add(getTask("test2", 20, true));
        finishedTasks.add(getTask("test3", 20, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(3, stats.size());
        assertTrue(stats.get(0).getTimePercentage() == 1.0/stats.size());
    }

    @Test
    public void twoFinishedTasksWithEqualTimeSpentAndEqualNames_SHOULD_generateStatisticWithPercentage100() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test", 20, true));
        finishedTasks.add(getTask("test", 20, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(1, stats.size());
        assertTrue(stats.get(0).getTimePercentage() == 1.0);
    }

    @Test
    public void twoFinishedTasksWithTimeSpentZero_SHOULD_generateStatisticWithTimeZero() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test", 0, true));
        finishedTasks.add(getTask("test", 0, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(1, stats.size());
        assertEquals(0, stats.get(0).getTimeSpentMinutes());
    }

    @Test
    public void openTask_SHOULD_NOT_generateStatistic() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test", 0, false));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(0, stats.size());
    }

    @Test
    public void openTaskAndFinishedTaskWithEqualNames_SHOULD_ignoreOpenTask() {
        //arrange
        List<Map<TaskValues, String>> subTasks = new ArrayList<>();
        List<Map<TaskValues, String>> finishedTasks = new ArrayList<>();
        finishedTasks.add(getTask("test", 10, false));
        finishedTasks.add(getTask("test", 5, true));
        when(databaseHelper.getTasks()).thenReturn(finishedTasks);
        when(databaseHelper.getSubTasks(Mockito.any())).thenReturn(subTasks);

        //act
        List<Statistic> stats = statService.getDalyStatistics();

        //assert
        assertEquals(1, stats.size());
        assertEquals(5, stats.get(0).getTimeSpentMinutes());
    }





    private Map<TaskValues, String> getTask(String taskName, int timeSpent, boolean finished){
        Map<TaskValues, String> taskObjectValues = new HashMap<>();
        taskObjectValues.put(TaskValues.ID, UUID.randomUUID().toString());
        taskObjectValues.put(TaskValues.NAME, taskName);
        taskObjectValues.put(TaskValues.TARGET_DATE, TaskDAO.getStringForDate(new Date(System.currentTimeMillis())));
        taskObjectValues.put(TaskValues.FINISH_DATE, TaskDAO.getStringForDate(new Date(System.currentTimeMillis())));
        taskObjectValues.put(TaskValues.TIME_SPENT, String.valueOf(timeSpent));
        taskObjectValues.put(TaskValues.FREQUENCY, Frequency.DAY.getKey());
        taskObjectValues.put(TaskValues.FINISHED, String.valueOf(finished));

        return taskObjectValues;
    }

    private Map<TaskValues, String> getSubTask() {
        Map<TaskValues, String> subTaskValues = new HashMap<>();
        subTaskValues.put(TaskValues.NAME, "egal");
        subTaskValues.put(TaskValues.FINISHED, String.valueOf(true));
        return  subTaskValues;
    }
}
