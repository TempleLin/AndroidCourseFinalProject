package com.templo.androidcoursefinalproject.room_database.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.templo.androidcoursefinalproject.room_database.model.User;

import java.util.List;

//This DAO(Data Access Object) will take care of the CRUD operations in Contact entity.
@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE) //Ignore if conflict happens.
    void insert(User user);

    @Query("DELETE FROM user_table") //"contact_table" gets auto-recognized by intellisense.
    void deleteAll();

    @Query("SELECT * FROM user_table ORDER BY name ASC") //ASC means ascending.
    LiveData<List<User>> getAllContacts(); //Data is encapsulated by LiveData.

    @Query("SELECT * FROM user_table WHERE email=:email AND name=:name AND password=:password")
    LiveData<User> getUser(String email, String name, String password);

    @Query("SELECT * FROM user_table WHERE email=:email AND password=:password")
    LiveData<User> getUser(String email, String password);

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE email=:email AND password=:password)")
    LiveData<Boolean> userExists(String email, String password);

    @Query("UPDATE user_table SET profile_pic = :profile_pic WHERE id=:id")
    void updateUserProfilePic(int id, String profile_pic);
}
