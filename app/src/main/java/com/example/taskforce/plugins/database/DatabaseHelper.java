package com.example.taskforce.plugins.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.taskforce.adapters.IDatabaseHelper;
import com.example.taskforce.adapters.TaskObjectValues;
import com.example.taskforce.application.TaskFactory;
import com.example.taskforce.domain.task.Frequency;
import com.example.taskforce.domain.task.SubTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper implements IDatabaseHelper {
    private static final String TAG = "DatebaseHelper";
    private static final String TABLE_NAME_TASK = "task_table";
    private static final String TABLE_NAME_SUBTASK = "subtask_table";

    private static final String COL_NAME = TaskObjectValues.NAME.getKey();
    private static final String COL_ID = TaskObjectValues.ID.getKey();
    private static final String COL_TARGET_DATE = TaskObjectValues.TARGET_DATE.getKey();
    private static final String COL_FINISH_DATE = TaskObjectValues.FINISH_DATE.getKey();
    private static final String COL_TIME_SPENT = TaskObjectValues.TIME_SPENT.getKey();
    private static final String COL_FREQUENCY = TaskObjectValues.FREQUENCY.getKey();
    private static final String COL_FINISHED = TaskObjectValues.FINISHED.getKey();


    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME_TASK, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTaskTable = "CREATE TABLE " + TABLE_NAME_TASK + " ("
                + COL_ID + " TEXT PRIMARY KEY, "
                + COL_NAME + " TEXT, "
                + COL_TARGET_DATE + " TEXT, "
                + COL_FINISH_DATE + " TEXT, "
                + COL_TIME_SPENT + " TEXT, "
                + COL_FREQUENCY + " TEXT, "
                + COL_FINISHED + " TEXT "
        + ")";

        String createSubTaskTable =  "CREATE TABLE " + TABLE_NAME_SUBTASK + " ("
                + COL_ID + " TEXT, "
                + COL_NAME + " TEXT, "
                + COL_FINISHED + " TEXT "
                + ")";

        db.execSQL(createTaskTable);
        db.execSQL(createSubTaskTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME_TASK);
        onCreate(db);
    }

    public void dropAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE " + TABLE_NAME_TASK);
        db.execSQL("DROP TABLE " + TABLE_NAME_SUBTASK);
    }

    @Override
    public boolean addTask(String taskObjectId, String taskName, String targetDate, String finishDate, String timeSpentMinutes, String frequency, String isFinished){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, taskObjectId);
        contentValues.put(COL_NAME, taskName);
        contentValues.put(COL_TARGET_DATE, targetDate);
        contentValues.put(COL_FINISH_DATE, finishDate);
        contentValues.put(COL_TIME_SPENT, timeSpentMinutes);
        contentValues.put(COL_FREQUENCY, frequency);
        contentValues.put(COL_FINISHED, isFinished);

        return db.insert(TABLE_NAME_TASK, null, contentValues) !=-1;
    }

    @Override
    public boolean addSubTask(String taskObjectId, SubTask sub){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, taskObjectId);
        contentValues.put(COL_NAME, sub.getTaskName());
        contentValues.put(COL_FINISHED, String.valueOf(sub.isFinished()));
        return db.insert(TABLE_NAME_SUBTASK, null, contentValues)!=-1;
    }

    @Override
    public List<Map<TaskObjectValues, String>> getTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_TASK;
        Cursor cursor = db.rawQuery(query, null);

        List<Map<TaskObjectValues, String>> taskObjects = new ArrayList<>();

        while(cursor.moveToNext()) {
            Map<TaskObjectValues, String> taskObjectValues = new HashMap<>();
            taskObjectValues.put(TaskObjectValues.ID, cursor.getString(cursor.getColumnIndex(COL_ID)));
            taskObjectValues.put(TaskObjectValues.NAME, cursor.getString(cursor.getColumnIndex(COL_NAME)));
            taskObjectValues.put(TaskObjectValues.TARGET_DATE, cursor.getString(cursor.getColumnIndex(COL_TARGET_DATE)));
            taskObjectValues.put(TaskObjectValues.FINISH_DATE, cursor.getString(cursor.getColumnIndex(COL_FINISH_DATE)));
            taskObjectValues.put(TaskObjectValues.TIME_SPENT, cursor.getString(cursor.getColumnIndex(COL_TIME_SPENT)));
            taskObjectValues.put(TaskObjectValues.FREQUENCY, cursor.getString(cursor.getColumnIndex(COL_FREQUENCY)));
            taskObjectValues.put(TaskObjectValues.FINISHED, cursor.getString(cursor.getColumnIndex(COL_FINISHED)));
            taskObjects.add(taskObjectValues);
        }

        return taskObjects;
    }

    public List<Map<TaskObjectValues, String>> getSubTasks(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_SUBTASK + " WHERE " + COL_ID + " = \"" + id + "\"";
        Cursor cursor = db.rawQuery(query, null);

        List<Map<TaskObjectValues, String>> subTasksForTaskObject = new ArrayList<>();

        while (cursor.moveToNext()){
            Map<TaskObjectValues, String> subTaskValues = new HashMap<>();
            subTaskValues.put(TaskObjectValues.NAME, cursor.getString(cursor.getColumnIndex(COL_NAME)));
            subTaskValues.put(TaskObjectValues.FINISHED, cursor.getString(cursor.getColumnIndex(COL_FINISHED)));
            subTasksForTaskObject.add(subTaskValues);
        }

        return subTasksForTaskObject;
    }

    public void updateTaskFinished(String taskId, String finishDate, String timeSpentMinutes, String isFinished){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_TASK + " SET "
                + COL_FINISHED + " = '" + isFinished + "', "
                + COL_FINISH_DATE + " = '" + finishDate + "', "
                + COL_TIME_SPENT + " = '" + timeSpentMinutes + "'"
                + " WHERE " + COL_ID + " = '" + taskId + "'";
        db.execSQL(query);
    }

    public void updateSubTaskFinished(String taskId, String name, boolean isFinished){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_SUBTASK + " SET " + COL_FINISHED +
                " = '" + String.valueOf(isFinished) + "' WHERE " + COL_ID + " = '" + taskId + "'"
        + " AND " + COL_NAME + " = '" + name + "'";
        db.execSQL(query);
    }

    public void deleteTask(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME_TASK + " WHERE "
                + COL_ID + " = '" + id + "'";

        Log.d(TAG, query);
        System.out.println("delete task with id: " + id + "\n" + query);
        db.execSQL(query);
    }

}
