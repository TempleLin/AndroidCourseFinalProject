package com.templo.androidcoursefinalproject.room_database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "category_table")
@ToString
public class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Getter@Setter
    private int id;

    @ColumnInfo(name = "name")
    @Getter@Setter
    private String name;

    public Category(String name) {
        this.name = name;
    }
}
