package com.templo.androidcoursefinalproject.room_database.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
}
