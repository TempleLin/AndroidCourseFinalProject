package com.templo.androidcoursefinalproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Notes:
 *  1.Profile picture's round bc its parent cardview's conerRadius is set to 100dp.
 */
public class ProfileFragment extends Fragment {

    private ImageView profilePicImgV;
    private Button loginBtn;

    private static boolean loggedIn = false;
    private static Uri profilePicAfterLoggedIn;
    private static String usernameAfterLoggedIn = "";

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePicImgV = requireView().findViewById(R.id.profile_pic_imgv);
        loginBtn = requireView().findViewById(R.id.login_btn);

        setChangeProfilePicOnClick();
        setLoginBtnLoginOnClick();
    }

    private void setLoginBtnLoginOnClick() {
        if (loggedIn) {
            deleteLoginBtn();
            profilePicImgV.setImageURI(profilePicAfterLoggedIn);
            return;
        }
        loginBtn.setOnClickListener(v -> {
            Intent loginIntent = new Intent(getActivity(), LoginRegisterActivity.class);
            loginResultLauncher.launch(loginIntent);
        });
    }

    private void setChangeProfilePicOnClick() {
        profilePicImgV.setOnClickListener(v -> {
            //Pick images from device. Tutorial reference: https://www.youtube.com/watch?v=H1ja8gvTtBE
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            selectProfileImgResultLauncher.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> selectProfileImgResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImg = data.getData();
                        profilePicImgV.setImageURI(selectedImg);
                        profilePicAfterLoggedIn = selectedImg; //Save image to buffer.
                    }
                }
            });
    private final ActivityResultLauncher<Intent> loginResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        loggedIn = data.getBooleanExtra("LoginSuccess", false);
                        if(loggedIn) {
                            usernameAfterLoggedIn = data.getStringExtra("UserName");
                            deleteLoginBtn();
                        } else {
                            Toast.makeText(getActivity(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    private void deleteLoginBtn() {
        //Delete button after logged in. Tutorial reference: https://stackoverflow.com/questions/3995215/add-and-remove-views-in-android-dynamically
        ViewGroup parent = (ViewGroup) loginBtn.getParent();
        if (parent != null) {
            parent.removeView(loginBtn);
            showUserNameLoginBtnDelete(parent);
        }
    }

    private void showUserNameLoginBtnDelete(ViewGroup parent) {
        TextView showUser = new TextView(getActivity());
        showUser.setText(usernameAfterLoggedIn);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(64, 0, 0, 0);
        showUser.setLayoutParams(params);
        showUser.setTextSize(20);
        parent.addView(showUser);
    }
}