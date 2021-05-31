package com.example.taskforce.plugins.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.taskforce.adapters.database.IDatabaseHelper;
import com.example.taskforce.adapters.database.TaskValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper implements IDatabaseHelper {
    private static final String TAG = "DatebaseHelper";
    private static final String TABLE_NAME_TASK = "task_table";
    private static final String TABLE_NAME_SUBTASK = "subtask_table";

    private static final String COL_NAME = TaskValues.NAME.getKey();
    private static final String COL_ID = TaskValues.ID.getKey();
    private static final String COL_TARGET_DATE = TaskValues.TARGET_DATE.getKey();
    private static final String COL_FINISH_DATE = TaskValues.FINISH_DATE.getKey();
    private static final String COL_TIME_SPENT = TaskValues.TIME_SPENT.getKey();
    private static final String COL_FREQUENCY = TaskValues.FREQUENCY.getKey();
    private static final String COL_FINISHED = TaskValues.FINISHED.getKey();


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
    public boolean addTask(Map<TaskValues, String> taskValues){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, taskValues.get(TaskValues.ID));
        contentValues.put(COL_NAME, taskValues.get(TaskValues.NAME));
        contentValues.put(COL_TARGET_DATE, taskValues.get(TaskValues.TARGET_DATE));
        contentValues.put(COL_FINISH_DATE, taskValues.get(TaskValues.FINISH_DATE));
        contentValues.put(COL_TIME_SPENT, taskValues.get(TaskValues.TIME_SPENT));
        contentValues.put(COL_FREQUENCY, taskValues.get(TaskValues.FREQUENCY));
        contentValues.put(COL_FINISHED, taskValues.get(TaskValues.FINISHED));

        boolean result = db.insert(TABLE_NAME_TASK, null, contentValues) !=-1;
        return result;
    }

    @Override
    public boolean addSubTask(Map<TaskValues, String> taskValues){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, taskValues.get(TaskValues.ID));
        contentValues.put(COL_NAME, taskValues.get(TaskValues.NAME));
        contentValues.put(COL_FINISHED, taskValues.get(TaskValues.FINISHED));
        boolean result = db.insert(TABLE_NAME_SUBTASK, null, contentValues)!=-1;
        return result;
    }

    @Override
    public List<Map<TaskValues, String>> getTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_TASK;
        Cursor cursor = db.rawQuery(query, null);

        List<Map<TaskValues, String>> taskObjects = new ArrayList<>();

        while(cursor.moveToNext()) {
            Map<TaskValues, String> taskObjectValues = new HashMap<>();
            taskObjectValues.put(TaskValues.ID, cursor.getString(cursor.getColumnIndex(COL_ID)));
            taskObjectValues.put(TaskValues.NAME, cursor.getString(cursor.getColumnIndex(COL_NAME)));
            taskObjectValues.put(TaskValues.TARGET_DATE, cursor.getString(cursor.getColumnIndex(COL_TARGET_DATE)));
            taskObjectValues.put(TaskValues.FINISH_DATE, cursor.getString(cursor.getColumnIndex(COL_FINISH_DATE)));
            taskObjectValues.put(TaskValues.TIME_SPENT, cursor.getString(cursor.getColumnIndex(COL_TIME_SPENT)));
            taskObjectValues.put(TaskValues.FREQUENCY, cursor.getString(cursor.getColumnIndex(COL_FREQUENCY)));
            taskObjectValues.put(TaskValues.FINISHED, cursor.getString(cursor.getColumnIndex(COL_FINISHED)));
            taskObjects.add(taskObjectValues);
        }
        cursor.close();
        return taskObjects;
    }

    @Override
    public Map<TaskValues, String> getTask(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_TASK + " WHERE " + COL_ID + " = \"" + id + "\"";
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        Map<TaskValues, String> taskObjectValues = new HashMap<>();
        taskObjectValues.put(TaskValues.ID, cursor.getString(cursor.getColumnIndex(COL_ID)));
        taskObjectValues.put(TaskValues.NAME, cursor.getString(cursor.getColumnIndex(COL_NAME)));
        taskObjectValues.put(TaskValues.TARGET_DATE, cursor.getString(cursor.getColumnIndex(COL_TARGET_DATE)));
        taskObjectValues.put(TaskValues.FINISH_DATE, cursor.getString(cursor.getColumnIndex(COL_FINISH_DATE)));
        taskObjectValues.put(TaskValues.TIME_SPENT, cursor.getString(cursor.getColumnIndex(COL_TIME_SPENT)));
        taskObjectValues.put(TaskValues.FREQUENCY, cursor.getString(cursor.getColumnIndex(COL_FREQUENCY)));
        taskObjectValues.put(TaskValues.FINISHED, cursor.getString(cursor.getColumnIndex(COL_FINISHED)));

        cursor.close();
        return taskObjectValues;
    }

    public List<Map<TaskValues, String>> getSubTasks(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_SUBTASK + " WHERE " + COL_ID + " = \"" + id + "\"";
        Cursor cursor = db.rawQuery(query, null);

        List<Map<TaskValues, String>> subTasksForTaskObject = new ArrayList<>();

        while (cursor.moveToNext()){
            Map<TaskValues, String> subTaskValues = new HashMap<>();
            subTaskValues.put(TaskValues.NAME, cursor.getString(cursor.getColumnIndex(COL_NAME)));
            subTaskValues.put(TaskValues.FINISHED, cursor.getString(cursor.getColumnIndex(COL_FINISHED)));
            subTasksForTaskObject.add(subTaskValues);
        }
        cursor.close();
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
        String queryTask = "DELETE FROM " + TABLE_NAME_TASK + " WHERE "
                + COL_ID + " = '" + id + "'";

        String querySubTask = "DELETE FROM " + TABLE_NAME_SUBTASK + " WHERE "
                + COL_ID + " = '" + id + "'";

        Log.d(TAG, queryTask);
        System.out.println("delete task with id: " + id + "\n" + queryTask);
        db.execSQL(queryTask);
        db.execSQL(querySubTask);
    }

}
