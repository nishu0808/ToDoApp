package com.example.todoapp.database;

import com.example.todoapp.model.ToDoList;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MyDao  {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addData(ToDoList myTodoList);

    @Query("select * from myTodoList order by mIsSelected asc")
    public List<ToDoList> getMyData();

    @Delete()
    public void delete(ToDoList toDoList);
}
