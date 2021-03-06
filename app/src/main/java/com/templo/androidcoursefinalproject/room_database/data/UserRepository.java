package com.templo.androidcoursefinalproject.room_database.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.templo.androidcoursefinalproject.room_database.model.User;
import com.templo.androidcoursefinalproject.room_database.util.TheDatabase;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allContacts; //Data is encapsulated by LiveData.

    public UserRepository(Application application) {
        TheDatabase db = TheDatabase.getDatabase(application);
        userDao = db.userDao();
        allContacts = userDao.getAllContacts();
    }

    public LiveData<List<User>> getAllData() { return allContacts; }
    public void insert(User user) {
        /*
        .execute(): Executes the given command at some time in the future. The command may execute in a new thread, in a pooled thread,
            or in the calling thread, at the discretion of the Executor implementation.
         */
        TheDatabase.databaseWriteExecutor.execute(() -> {
            //.insert must be used in the background thread, that's why it's in .execute callback.
            userDao.insert(user);
        });
    }

    public LiveData<User> getUser(Application application, int id) {
        TheDatabase db = TheDatabase.getDatabase(application);
        return db.userDao().getUser(id);
    }

    public LiveData<User> getUser(Application application, String email, String name, String password) {
        TheDatabase db = TheDatabase.getDatabase(application);
        return db.userDao().getUser(email, name, password);
    }

    public LiveData<User> getUser(Application application, String email, String password) {
        TheDatabase db = TheDatabase.getDatabase(application);
        return db.userDao().getUser(email, password);
    }

    public LiveData<Boolean> userExists(Application application, String email, String password) {
        TheDatabase db = TheDatabase.getDatabase(application);
        return db.userDao().userExists(email, password);
    }

    public void updateUserProfilePic(Application application, int id, String profile_pic) {
        TheDatabase db = TheDatabase.getDatabase(application);
        db.userDao().updateUserProfilePic(id, profile_pic);
    }

    public void deleteAll(Application application) {
        TheDatabase db = TheDatabase.getDatabase(application);
        db.userDao().deleteAll();
    }
}
