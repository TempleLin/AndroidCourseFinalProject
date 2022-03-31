package com.templo.androidcoursefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginRegisterActivity extends AppCompatActivity {

    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        loginBtn = findViewById(R.id.login_check_btn);
        loginBtn.setOnClickListener(v -> {
            Intent intent = getIntent().putExtra("LoginSuccess", true);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}