package com.example.taskforce.adapters.database;

import com.example.taskforce.application.ITaskObjectDAO;
import com.example.taskforce.application.TaskFactory;
import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.domain.task.SubTask;
import com.example.taskforce.domain.task.Task;
import com.example.taskforce.domain.task.TaskObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskObjectDAO implements ITaskObjectDAO {
    private final IDatabaseHelper dbHelper;

    public TaskObjectDAO(IDatabaseHelper databaseHelper){
        this.dbHelper =databaseHelper;
    }

    public List<TaskObject> getAllTaskObjects(){
        List<TaskObject> taskObjects = new ArrayList<>();
        List<Map<TaskObjectValues, String>> taskValues = dbHelper.getTasks();

        return taskValues.stream()
                .map(taskWithStringValues -> generateTaskObjectFromStringValues(taskWithStringValues))
                .collect(Collectors.toList());
    }

    private TaskObject generateTaskObjectFromStringValues(Map<TaskObjectValues, String> taskObjectWithStringValues) {
        TaskFactory fac = new TaskFactory();

        UUID objId = UUID.fromString(taskObjectWithStringValues.get(TaskObjectValues.ID));
        fac.setTaskName(taskObjectWithStringValues.get(TaskObjectValues.NAME));

        try {
            fac.setTargetDate(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(taskObjectWithStringValues.get(TaskObjectValues.TARGET_DATE)));
            fac.setFinishDate(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(taskObjectWithStringValues.get(TaskObjectValues.FINISH_DATE)));
            fac.setFrequency(Frequency.fromKey(taskObjectWithStringValues.get(TaskObjectValues.FREQUENCY)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        fac.setTimeSpentMinutes(Integer.valueOf(taskObjectWithStringValues.get(TaskObjectValues.TIME_SPENT)));
        fac.setFinished(Boolean.valueOf(taskObjectWithStringValues.get(TaskObjectValues.FINISHED)));

        List<Map<TaskObjectValues, String>> subTasksWithStringValues = dbHelper.getSubTasks(objId.toString());
        List<SubTask> subTasks = subTasksWithStringValues.stream()//
                .map(sub -> generateSubTaskFromStringValues(sub))//
                .collect(Collectors.toList());

        fac.setSubTasks(subTasks);
        return fac.build(objId);
    }

    private SubTask generateSubTaskFromStringValues(Map<TaskObjectValues, String> subTaskValues){
        String subTaskName = subTaskValues.get(TaskObjectValues.NAME);
        boolean isSubTaskFinished = Boolean.valueOf(subTaskValues.get(TaskObjectValues.FINISHED));
        return new SubTask(subTaskName, isSubTaskFinished);
    }

    public List<TaskObject> getAllOpenTasks(){
        return getAllTaskObjects().stream()//
                    .filter(task -> !task.isFinished())//
                    .collect(Collectors.toList());
    }

    public List<TaskObject> getAllFinishedTasks(){
        return getAllTaskObjects().stream()//
                .filter(TaskObject::isFinished)//
                .collect(Collectors.toList());
    }


    public boolean saveTaskToDatabase(TaskObject taskObject){
        Task task = taskObject.getTask();
        boolean worked = dbHelper.addTask(
                taskObject.getId().toString(),
                task.getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(task.getTargetDate()),
                new SimpleDateFormat("yyyy-MM-dd").format(taskObject.getFinishDate()),
                String.valueOf(taskObject.getTimeSpentMinutes()),
                task.getFrequency().getKey(),
                String.valueOf(taskObject.isFinished()));

        for(SubTask sub: taskObject.getSubTasks()){
            worked &= dbHelper.addSubTask(taskObject.getId().toString(), sub);
        }
        return worked;
    }

    public void updateTask(TaskObject taskObject){
        dbHelper.updateTaskFinished(
                taskObject.getId().toString(),
                new SimpleDateFormat("yyyy-MM-dd").format(taskObject.getFinishDate()),
                String.valueOf(taskObject.getTimeSpentMinutes()),
                String.valueOf(taskObject.isFinished()));
    }

    public void updateSubTask(UUID taskId, SubTask subTask){
        dbHelper.updateSubTaskFinished(taskId.toString(), subTask.getTaskName(), subTask.isFinished());
    }

    public void deleteTaskFromDatabase(UUID id){
        dbHelper.deleteTask(id.toString());
    }
}
