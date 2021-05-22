package com.example.taskforce.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.taskforce.task.SubTask;
import com.example.taskforce.task.Task;

import java.text.SimpleDateFormat;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatebaseHelper";
    private static final String TABLE_NAME_TASK = "task_table";
    private static final String TABLE_NAME_SUBTASK = "subtask_table";
    private static final String COL_NAME = "name";
    private static final String COL_ID = "ID";
    private static final String COL_TARGET_DATE = "target_date";
    private static final String COL_FINISH_DATE = "finish_date";
    private static final String COL_FREQUENCY = "frequency";
    private static final String COL_FINISHED = "finished";


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

    public boolean addTask(String taskObjectId, String taskName, String targetDate, String finishDate, String frequency, boolean isFinished){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, taskObjectId);
        contentValues.put(COL_NAME, taskName);
        contentValues.put(COL_TARGET_DATE, targetDate);
        contentValues.put(COL_FINISH_DATE, finishDate);
        contentValues.put(COL_FREQUENCY, frequency);
        contentValues.put(COL_FINISHED, String.valueOf(isFinished));

        return db.insert(TABLE_NAME_TASK, null, contentValues) !=-1;
    }

    public boolean addSubTask(String taskObjectId, SubTask sub){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, taskObjectId);
        contentValues.put(COL_NAME, sub.getTaskName());
        contentValues.put(COL_FINISHED, String.valueOf(sub.isFinished()));
        return db.insert(TABLE_NAME_SUBTASK, null, contentValues)!=-1;
    }

    public Cursor getTask(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_TASK;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSubTasks(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_SUBTASK + " WHERE " + COL_ID + " = \"" + id + "\"";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateTaskFinished(String taskId, boolean isFinished){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_TASK + " SET " + COL_FINISHED +
                " = '" + String.valueOf(isFinished) + "' WHERE " + COL_ID + " = '" + taskId + "'";
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
