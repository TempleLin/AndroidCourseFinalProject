package com.templo.androidcoursefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.templo.androidcoursefinalproject.room_database.model.User;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

/**
 * Plans:
 *  Use WebView to place gallery of images for the shop items.
 *
 * Known Issues:
 *  1.Directories inside the directory for the website to show to WebView shouldn't have
 *      sub-folders with "_" prefix. it will cause loading assets for website generate error.
 *  2.Calling getResources().getString() or .getString() inside fragment might cause errors.
 *      Use getActivity().getApplication().getString() instead.
 *
 * Notes:
 *  1.Networking is turned on. (<uses-permission android:name="android.permission.INTERNET" /> under AndroidManifest.xml.)
 *  2.Title bar is removed. ("Theme.AppCompat.Light.DarkActionBar" under themes.xml changed to "Theme.AppCompat.Light.NoActionBar".)
 *  3.BottomNavbar is used from Google Material library.
 *      "implementation 'com.google.android.material:material:1.5.0'" is added to build.gradle.
 *      Tutorial reference:
 *          1.https://code.tutsplus.com/tutorials/how-to-code-a-bottom-navigation-bar-for-an-android-app--cms-30305
 *          2.Last step to setup Controller, use the second answer to this post's question:
 *              https://stackoverflow.com/questions/50577356/android-jetpack-navigation-bottomnavigationview-with-youtube-or-instagram-like
 *  4.tsparticles library used in webview might not work with some lower versions of Android phone.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_fragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

        startupROOMDatabase();
    }

    private void startupROOMDatabase() {
        //Remember to instantiate this ViewModel object like this before calling its class's static methods.
        //  (ex. .insert())
        UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(UserViewModel.class);

        //This callback passed to .observe() gets called when the LiveData gets edited in real time.
        //  Therefore, the textview updates its text values whenever .insert() adds values to the LiveData.
        userViewModel.getAllUsers().observe(this, users -> {
            Log.d("TAG", "Observer called!");
            StringBuilder toShow = new StringBuilder();
            for(User user : users) {
                toShow.append(user.toString());
            }
            //This debug.log might not work, but the value is correct.
            Log.d("ROOM_DATABASE", toShow.toString());
        });

//        UserRoomDatabase.databaseWriteExecutor.execute(() -> {
//            UserRoomDatabase.getDatabase(this).contactDao().deleteAll();
//        });
//

//        UserViewModel.insertOnlyOne(getApplication(), new User("John", "test@jmail.com", "1234"));
//        userViewModel.getUser(getApplication(), "test@jmail.com", "John", "1234").observe(this, user -> {
//            if (user == null) {
//                Snackbar.make(findViewById(R.id.bottom_navigation_view), "USER NULL", Snackbar.LENGTH_SHORT).show();
//            } else {
//                Snackbar.make(findViewById(R.id.bottom_navigation_view), user.toString(), Snackbar.LENGTH_SHORT).show();
//            }
//        });
    }
}