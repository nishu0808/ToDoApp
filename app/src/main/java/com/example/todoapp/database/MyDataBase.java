package com.example.todoapp.database;

import com.example.todoapp.model.ToDoList;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={ToDoList.class},version = 1)
public abstract class MyDataBase extends RoomDatabase {
    public abstract MyDao myDao();
}



