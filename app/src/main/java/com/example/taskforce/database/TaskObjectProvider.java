package com.example.taskforce.database;

import android.content.Context;
import android.database.Cursor;

import com.example.taskforce.task.Frequency;
import com.example.taskforce.task.SubTask;
import com.example.taskforce.task.TaskFactory;
import com.example.taskforce.task.TaskObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskObjectProvider {

    public static List<TaskObject> getAllTaskObjects(Context context){
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
                fac.setFrequency(Frequency.fromKey(cursor.getString(3)));
            }catch (Exception e){
                System.out.println(e);
                break;
            }

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

    public static boolean saveTaskToDatabase(Context context, TaskObject taskObject){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        boolean worked = dbHelper.addTask(taskObject.getId().toString(), taskObject.getTask(), taskObject.isFinished());
        for(SubTask sub: taskObject.getSubTasks()){
            worked &= dbHelper.addSubTask(taskObject.getId().toString(), sub);
        }
        return worked;
    }

    public static void deleteTaskFromDatabase(Context context, UUID id){
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.deleteTask(id.toString());
    }
}
