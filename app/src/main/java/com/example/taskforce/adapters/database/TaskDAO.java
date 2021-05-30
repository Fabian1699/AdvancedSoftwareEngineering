package com.example.taskforce.adapters.database;

import com.example.taskforce.application.ITaskDAO;
import com.example.taskforce.application.TaskFactory;
import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.domain.task.TaskBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskDAO implements ITaskDAO {
    private final IDatabaseHelper dbHelper;

    public TaskDAO(IDatabaseHelper databaseHelper){
        this.dbHelper =databaseHelper;
    }

    public List<Task> getAllTaskObjects(){
        List<Task> taskObjects = new ArrayList<>();
        List<Map<TaskValues, String>> taskValues = dbHelper.getTasks();

        return taskValues.stream()
                .map(taskWithStringValues -> generateTaskFromStringValues(taskWithStringValues, new HashSet<>()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Optional<Task> getTask(UUID id){
        return generateTaskFromStringValues(dbHelper.getTask(id.toString()), new HashSet<>());
    }

    public Optional<Task> generateTaskFromStringValues(Map<TaskValues, String> taskObjectWithStringValues, Set<String> subTasks) {
        TaskFactory fac = new TaskFactory();

        String name = taskObjectWithStringValues.getOrDefault(TaskValues.NAME, null);
        String objId = taskObjectWithStringValues.getOrDefault(TaskValues.ID, null);
        String targetDate = taskObjectWithStringValues.getOrDefault(TaskValues.TARGET_DATE, null);
        String finishDate = taskObjectWithStringValues.getOrDefault(TaskValues.FINISH_DATE, null);
        String frequency = taskObjectWithStringValues.getOrDefault(TaskValues.FREQUENCY, null);
        String timeSpentMinutes = taskObjectWithStringValues.getOrDefault(TaskValues.TIME_SPENT, null);
        String finished = taskObjectWithStringValues.getOrDefault(TaskValues.FINISHED, null);


        if(name!=null){fac.setTaskName(name);}
        if(objId!=null){fac.setId(UUID.fromString(objId));}
        if(timeSpentMinutes!=null){fac.setTimeSpentMinutes(Integer.valueOf(timeSpentMinutes));}
        if(finished!=null){fac.setFinished(Boolean.valueOf(finished));}

        try {
            if(targetDate!=null){fac.setTargetDate(getDateForString(targetDate));}
            if(finishDate!=null){fac.setFinishDate(getDateForString(finishDate));}
            if(frequency!=null){fac.setFrequency(Frequency.fromKey(frequency));}
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(subTasks.isEmpty()) {
            List<Map<TaskValues, String>> subTasksWithStringValues = dbHelper.getSubTasks(objId.toString());
            fac.setSubTasks(subTasksWithStringValues.stream()//
                    .map(sub -> generateSubTaskFromStringValues(sub))//
                    .collect(Collectors.toSet()));

        }else{
            fac.setSubTasksFromString(subTasks);
        }
        return fac.build();
    }

    private SubTask generateSubTaskFromStringValues(Map<TaskValues, String> subTaskValues){
        String subTaskName = subTaskValues.get(TaskValues.NAME);
        boolean isSubTaskFinished = Boolean.valueOf(subTaskValues.get(TaskValues.FINISHED));
        return new SubTask(subTaskName, isSubTaskFinished);
    }

    public boolean saveTaskToDatabase(Task task){
        TaskBase taskBase = task.getTaskObjectCopy().getTaskBase();

        boolean worked = dbHelper.addTask(
                task.getId().toString(),
                taskBase.getName(),
                getStringForDate(taskBase.getTargetDate()),
                getStringForDate(taskBase.getTargetDate()),
              //  new SimpleDateFormat("yyyy-MM-dd").format(task.getFinishDate()),
              //  String.valueOf(task.getTimeSpentMinutes()),
                String.valueOf(0),
                taskBase.getFrequency().getKey(),
                String.valueOf(task.isFinished()));

        for(SubTask sub: task.getSubTasks()){
            worked &= dbHelper.addSubTask(task.getId().toString(), sub.getTaskName(), String.valueOf(sub.isFinished()));
        }
        return worked;
    }

    public static String getStringForDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static Date getDateForString(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    public void updateTask(Task task){
        dbHelper.deleteTask(task.getId().toString());
        saveTaskToDatabase(task);
/*        dbHelper.updateTaskFinished(
                taskObject.getId().toString(),
                new SimpleDateFormat("yyyy-MM-dd").format(taskObject.getFinishDate()),
                String.valueOf(taskObject.getTimeSpentMinutes()),
                String.valueOf(taskObject.isFinished()));

 */
    }

    public void updateSubTask(UUID taskId, SubTask subTask){
        dbHelper.updateSubTaskFinished(taskId.toString(), subTask.getTaskName(), subTask.isFinished());
    }

    public void deleteTaskFromDatabase(UUID id){
        dbHelper.deleteTask(id.toString());
    }
}
