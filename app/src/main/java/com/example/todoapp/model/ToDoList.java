package com.example.todoapp.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="myTodoList")
public class ToDoList {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "mTitle")
    private String mTitle;

    @ColumnInfo(name = "mDescription")
    private String mDescription;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] mImage;

    private boolean mIsSelected;

    public ToDoList(@NonNull String title, String description, byte[] image, boolean isSelected) {
        this.mTitle       = title;
        this.mDescription = description;
        this.mImage       = image;
        this.mIsSelected  = isSelected;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public byte[] getImage() { return mImage; }
    
    public boolean isSelected() {
        return mIsSelected;
    }
}
