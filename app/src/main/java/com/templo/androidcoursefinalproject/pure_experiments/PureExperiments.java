package com.templo.androidcoursefinalproject.pure_experiments;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.templo.androidcoursefinalproject.room_database.model.User;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

public class PureExperiments {
    public static void testROOMDatabase(LifecycleOwner owner) {
        //Remember to instantiate this ViewModel object like this before calling its class's static methods.
        //  (ex. .insert())
        UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory((Application) owner)
                .create(UserViewModel.class);

        //This callback passed to .observe() gets called when the LiveData gets edited in real time.
        //  Therefore, the textview updates its text values whenever .insert() adds values to the LiveData.
        userViewModel.getAllUsers().observe(owner, users -> {
            Log.d("TAG", "Observer called!");
            StringBuilder toShow = new StringBuilder();
            for(User user : users) {
                toShow.append(user.getName())
                        .append('-')
                        .append(user.getEmail())
                        .append('-')
                        .append(user.getPassword())
                        .append('\n');
            }
            //This debug.log might not work, but the value is correct.
            Log.d("ROOM_DATABASE", toShow.toString());
        });
    }
}
