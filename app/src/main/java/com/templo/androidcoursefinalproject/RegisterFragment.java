package com.templo.androidcoursefinalproject;

import static android.app.Activity.RESULT_OK;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class RegisterFragment extends Fragment {
    private Activity activity;
    private Application application;

    private EditText editTextRegEmail;
    private EditText editTextRegUserName;
    private EditText editTextRegPassword;
    private Button registerBtn;
    private TextView backToLoginTextView;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = requireActivity();
        application = activity.getApplication();

        editTextRegEmail = activity.findViewById(R.id.editTextRegEmail);
        editTextRegUserName = activity.findViewById(R.id.editTextRegName);
        editTextRegPassword = activity.findViewById(R.id.editTextRegPassword);
        registerBtn = activity.findViewById(R.id.register_check_btn);
        backToLoginTextView = activity.findViewById(R.id.back_to_login_textview);

        backToLoginTextView.setOnClickListener(v -> {
            ((LoginRegisterActivity)activity).switchBetweenRegisterAndLoginFragment();
        });
    }
}