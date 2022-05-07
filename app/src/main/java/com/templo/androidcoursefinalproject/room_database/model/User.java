package com.templo.androidcoursefinalproject.room_database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Entity mapping to the actual table in database.
@Entity(tableName = "user_table")
@ToString
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @Getter@Setter
    private int id; //Primary key id column of the table.

    @ColumnInfo(name = "name")
    @Getter@Setter
    private String name;

    @ColumnInfo(name = "email")
    @Getter@Setter
    private String email;

    @ColumnInfo(name = "password")
    @Getter@Setter
    private String password;

    @ColumnInfo(name = "profile_pic")
    @Getter@Setter
    private String profile_pic;

    public User(String name, String email, String password, String profile_pic) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_pic = profile_pic;
    }
}
