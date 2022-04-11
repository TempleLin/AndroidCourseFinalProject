package com.templo.androidcoursefinalproject;

import android.app.Activity;
import android.app.Application;
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

import com.templo.androidcoursefinalproject.room_database.model.User;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterFragment extends Fragment {
    private Activity activity;
    private Application application;

    private EditText editTextRegEmail;
    private EditText editTextRegUserName;
    private EditText editTextRegPassword;
    private EditText editTextRegPasswordAgain;
    private Button registerBtn;
    private TextView backToLoginTextView;
    private TextView registerResultTextView;

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
        editTextRegPasswordAgain = activity.findViewById(R.id.editTextRegPasswordAgain);
        registerBtn = activity.findViewById(R.id.register_check_btn);
        backToLoginTextView = activity.findViewById(R.id.back_to_login_textview);
        registerResultTextView = activity.findViewById(R.id.registerResultTextView);

        //Hide password by default.
        editTextRegPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editTextRegPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());

        registerBtn.setOnClickListener(v -> {
            String regEmailVal = editTextRegEmail.getText().toString();
            String regUserNameVal = editTextRegUserName.getText().toString();
            String regPasswordVal = editTextRegPassword.getText().toString();
            String regPasswordAgainVal = editTextRegPasswordAgain.getText().toString();

            if (!regPasswordVal.equals(regPasswordAgainVal)) {
                registerResultTextView.setText(R.string.two_password_not_identical);
                return;
            }

            if (!regEmailVal.equals("") && !regUserNameVal.equals("") && !regPasswordVal.equals("")) {
                UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                        .create(UserViewModel.class);

                userViewModel.userExists(application, regEmailVal, regPasswordVal).observe((LifecycleOwner) activity, exists -> {
                    if (exists) {
                        registerResultTextView.setText(application.getString(R.string.user_already_exists));
                    } else {
                        UserViewModel.insertOnlyOne(application, new User(regUserNameVal, regEmailVal, regPasswordVal, null));
                        registerResultTextView.setText("");
                        ((LoginRegisterActivity)activity).switchBetweenRegisterAndLoginFragment();
                    }
                });
            } else {
                registerResultTextView.setText(application.getString(R.string.input_cannot_empty));
            }
        });

        backToLoginTextView.setOnClickListener(v -> {
            ((LoginRegisterActivity)activity).switchBetweenRegisterAndLoginFragment();
        });
    }
}