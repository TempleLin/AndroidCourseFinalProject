package com.templo.androidcoursefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Plans:
 *  Use WebView to place gallery of images for the shop items.
 *
 * Known Issues:
 *  1.Directories inside the directory for the website to show to WebView shouldn't have
 *      sub-folders with "_" prefix. it will cause loading assets for website generate error.
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
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigatin_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_fragment);
        assert navHostFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_for_items_web);
//        if (fragment == null) {
//            fragment = SelectionItemFrag.newInstance();
//            fragmentManager.beginTransaction()
//                    .add(R.id.frame_for_items_web, fragment)
//                    .commit();
//        }
        ImageButton cartBtn = findViewById(R.id.shopping_cart_btn);
        cartBtn.setOnClickListener(v -> {
            Log.d("TAG", "Click cart button!");
        });
    }
}