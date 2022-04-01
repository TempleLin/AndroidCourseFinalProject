package com.templo.androidcoursefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;

public class LoginRegisterActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        loginBtn = findViewById(R.id.login_check_btn);
        editTextPassword = findViewById(R.id.editTextPassword);

        loginBtn.setOnClickListener(v -> {
            Intent intent = getIntent().putExtra("LoginSuccess", true);
            setResult(RESULT_OK, intent);
            finish();
        });

        //Hide password by default.
        editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}