package com.templo.androidcoursefinalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout_for_fragment);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.frame_layout_for_fragment, fragment)
                    .commit();
        }
    }

    //Gets called from fragment.
    public void switchBetweenRegisterAndLoginFragment() {
        /*
         * .replace() replaces fragment in the container layout with new fragment.
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout_for_fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment instanceof LoginFragment) {
            transaction.replace(R.id.frame_layout_for_fragment, RegisterFragment.class, null);
        } else if (fragment instanceof RegisterFragment) {
            transaction.replace(R.id.frame_layout_for_fragment, LoginFragment.class, null);
        }
        transaction.commit();
    }
}