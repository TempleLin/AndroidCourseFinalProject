package com.templo.androidcoursefinalproject.room_database.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.templo.androidcoursefinalproject.room_database.data.UserRepository;

import java.util.List;

/*
ViewModel holds all the data (LiveData) needed for the UI. UI is notified of changes using "observation".
 */
public class UserViewModel extends AndroidViewModel {

    public static UserRepository repository;
    public final LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUsers = repository.getAllData();
    }

    public LiveData<List<User>> getAllUsers() { return allUsers; }
    public static void insert(Application application, User user) {
        if (repository == null) {
            new ViewModelProvider.AndroidViewModelFactory(application)
                    .create(UserViewModel.class);
        }
        repository.insert(user);
    }
//    public static void insertOnlyOne(Application application, User user) { //User must be unique.
//        if (repository.getUser(application, user.getEmail(), user.getName(), user.getPassword()) != null) {
//            repository.insert(user);
//        }
//    }
    public LiveData<User> getUser(Application application, String email, String name, String password) {
        return repository.getUser(application, email, name, password);
    }

    public LiveData<User> getUser(Application application, String email, String password) {
        return repository.getUser(application, email, password);
    }

    public LiveData<Boolean> userExists(Application application, String email, String password) {
        return repository.userExists(application, email, password);
    }

    public void updateUserProfilePic(Application application, int id, String profile_pic) {
        repository.updateUserProfilePic(application, id, profile_pic);
    }
}
