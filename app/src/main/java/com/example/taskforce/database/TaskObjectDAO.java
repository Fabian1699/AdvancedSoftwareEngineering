package com.example.taskforce.database;

import android.content.Context;
import android.database.Cursor;

import com.example.taskforce.task.Frequency;
import com.example.taskforce.task.SubTask;
import com.example.taskforce.task.Task;
import com.example.taskforce.task.TaskFactory;
import com.example.taskforce.task.TaskObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskObjectDAO {
    private final Context context;

    public TaskObjectDAO(Context context){
        this.context=context;
    }

    public List<TaskObject> getAllTaskObjects(){
        List<TaskObject> taskObjects = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Cursor cursor = dbHelper.getTask(null);
        while(cursor.moveToNext()){
            List<SubTask> subTasks = new ArrayList<>();
            TaskFactory fac = new TaskFactory();
            UUID objId = UUID.fromString(cursor.getString(0));
            fac.setTaskName(cursor.getString(1));
            try{
                fac.setTargetDate(new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(2)));
                fac.setFinishDate(new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(3)));
                fac.setFrequency(Frequency.fromKey(cursor.getString(4)));
            }catch (Exception e){
                System.out.println(e);
                break;
            }
            fac.setFinished(Boolean.valueOf(cursor.getString(5)));

            Cursor subCursor = dbHelper.getSubTasks(objId.toString());
            while (subCursor.moveToNext()){
                String subTaskName = subCursor.getString(1);
                boolean isSubTaskFinished = Boolean.valueOf(subCursor.getString(2));
                subTasks.add(new SubTask(subTaskName, isSubTaskFinished));
            }
            fac.setSubTasks(subTasks);
            taskObjects.add(fac.build(objId));
        }
        return taskObjects;
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
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Task task = taskObject.getTask();
        boolean worked = dbHelper.addTask(
                taskObject.getId().toString(),
                task.getName(),
                new SimpleDateFormat("yyyy-MM-dd").format(task.getTargetDate()),
                new SimpleDateFormat("yyyy-MM-dd").format(taskObject.getFinishDate()),
                task.getFrequency().getKey(),
                taskObject.isFinished());

        for(SubTask sub: taskObject.getSubTasks()){
            worked &= dbHelper.addSubTask(taskObject.getId().toString(), sub);
        }
        return worked;
    }

    public void updateTask(TaskObject taskObject){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.updateTaskFinished(taskObject.getId().toString(), taskObject.isFinished());
    }

    public void updateSubTask(UUID taskId, SubTask subTask){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.updateSubTaskFinished(taskId.toString(), subTask.getTaskName(), subTask.isFinished());
    }

    public void deleteTaskFromDatabase(UUID id){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.deleteTask(id.toString());
    }
}
