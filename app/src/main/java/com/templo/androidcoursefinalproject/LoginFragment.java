package com.templo.androidcoursefinalproject;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private Activity activity;
    private Application application;

    private Button loginBtn;
    private TextView registerTextView;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView loginResultTextView;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = requireActivity();
        application = activity.getApplication();

        loginBtn = activity.findViewById(R.id.login_check_btn);
        registerTextView = activity.findViewById(R.id.registerTextView);
        editTextEmail = activity.findViewById(R.id.editTextEmail);
        editTextPassword = activity.findViewById(R.id.editTextPassword);
        loginResultTextView = activity.findViewById(R.id.loginResultTextView);

        registerTextView.setOnClickListener(v -> {
            ((LoginRegisterActivity)activity).switchBetweenRegisterAndLoginFragment();
        });

        loginBtn.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (!email.equals("") && !password.equals("")) {
                UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                        .create(UserViewModel.class);
                userViewModel.userExists(application, email, password).observe((LifecycleOwner) activity, exists -> {
                    if (exists) {
                        loginResultTextView.setText("");
                        Intent intent = activity.getIntent().putExtra("LoginSuccess", true)
                                .putExtra("Email", email)
                                .putExtra("Password", password);
                        activity.setResult(RESULT_OK, intent);
                        activity.finish();
                    } else {
                        loginResultTextView.setText(getResources().getString(R.string.login_details_not_match));
                    }
                });
            } else {
                loginResultTextView.setText(getResources().getString(R.string.input_cannot_empty));
            }
        });

        //Hide password by default.
        editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}