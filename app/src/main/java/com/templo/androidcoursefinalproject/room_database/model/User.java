package com.templo.androidcoursefinalproject.room_database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entity mapping to the actual table in database.
@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id; //Primary key id column of the table.

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "profile_pic")
    private String profile_pic;

    public User(String name, String email, String password, String profile_pic) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_pic = profile_pic;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getProfile_pic() {
        return profile_pic;
    }
    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
