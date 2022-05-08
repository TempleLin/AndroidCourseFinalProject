package com.templo.androidcoursefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.templo.androidcoursefinalproject.room_database.model.Category;
import com.templo.androidcoursefinalproject.room_database.model.CategoryViewModel;
import com.templo.androidcoursefinalproject.room_database.util.TheDatabase;

/**
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
 *  5.In order to use AutocompleteSupportFragment for searching location for Google Map fragment, must pay for billing. Therefore, its purpose
 *    in this profile will be just for demonstration. (It's located under activity_maps_select_loc.xml activity.)
 *
 *  Important Features implemented:
 *  1.ROOM Database. It's controlling SQLite under the hood, to save and use user accounts, and shop items. User's profile thumbnail is also saved.
 *  2.LocationManager. It's used in this project for detecting current location of the user.
 *  3.Google Map API. It's used in this project for showing locations.
 *  4.Google Places API. It's used in this project for AutocompleteSupportFragment which let's user search for addresses. But as stated above, it
 *    requires billing. So it's just for demonstration purpose.
 *  5.Geocoder. To convert longitude and latitude to address.
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
        TheDatabase theDatabase = TheDatabase.getDatabase(getApplicationContext());

        setupCategoriesTableDefaultRecords();

//        ProductViewModel productViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
//                .create(ProductViewModel.class);
//        //product_table has foreign keys to other tables; ROOM database might do executions in an asynchronous stack approach.
//        // But, if the foreign key relative to another table's row doesn't exist yet, insertion will fail. Therefore, execute insert to product_table first.
//        ProductViewModel.insert(getApplication(), new Product("Test",
//                1, null, null, null, null, null, 1, "Location", "Description"));
//
//        UserViewModel.insert(getApplication(), new User("John", "john1234@gmail.com", "1234", null));
//
//        CategoryViewModel categoryViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
//                .create(CategoryViewModel.class);
//        CategoryViewModel.insert(getApplication(), new Category("First"));
//
//        RelationalViewModel relationalViewModel =  new ViewModelProvider.AndroidViewModelFactory(getApplication())
//                .create(RelationalViewModel.class);
//        relationalViewModel.getAllUsersWithProducts(getApplication()).observe(MainActivity.this, all -> {
//            Log.d("Relational", all.toString());
//        });
//        relationalViewModel.getAllCategoriesWithProducts(getApplication());

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

    private void setupCategoriesTableDefaultRecords() {
        CategoryViewModel categoryViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(CategoryViewModel.class);

        String[] allDefaultCategories = new String[] {
            "Furniture", "Outdoor", "Pets", "Video Games"
        };

        categoryViewModel.getAllCategories().observe(MainActivity.this, all -> {
            if (all.size() != allDefaultCategories.length) {
                CategoryViewModel.deleteAll(getApplication());
                for (String defaultCategory : allDefaultCategories) {
                    CategoryViewModel.insert(MainActivity.this.getApplication(),
                            new Category(defaultCategory));
                }
            }
        });
    }
}