package com.example.ToDoList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TASKS = "tasks";

    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESC = "description";
    private static final String COL_DUE = "due_date";
    private static final String COL_PRIORITY = "priority";
    private static final String COL_COMPLETED = "is_completed";

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_TASKS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESC + " TEXT, " +
                COL_DUE + " TEXT, " +
                COL_PRIORITY + " TEXT, " +
                COL_COMPLETED + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Add Task
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, task.getTitle());
        values.put(COL_DESC, task.getDescription());
        values.put(COL_DUE, task.getDueDate());
        values.put(COL_PRIORITY, task.getPriority());
        values.put(COL_COMPLETED, task.isCompleted() ? 1 : 0);
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    // Get All Tasks
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DUE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_PRIORITY)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPLETED)) == 1
                );
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    // Update Task
    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, task.getTitle());
        values.put(COL_DESC, task.getDescription());
        values.put(COL_DUE, task.getDueDate());
        values.put(COL_PRIORITY, task.getPriority());
        values.put(COL_COMPLETED, task.isCompleted() ? 1 : 0);

        db.update(TABLE_TASKS, values, COL_ID + "=?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    // Delete Task
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}

