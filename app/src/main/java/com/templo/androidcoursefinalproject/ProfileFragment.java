package com.templo.androidcoursefinalproject;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.templo.androidcoursefinalproject.custom_list.*;
import com.templo.androidcoursefinalproject.custom_list.CustomRow;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Notes:
 *  1.Profile picture's round bc its parent cardview's conerRadius is set to 100dp.
 */
public class ProfileFragment extends Fragment {
    private Application application;

    private ImageView profilePicImgV;
    private Button loginBtn;
    private ListView profileOptionsListView;

    private static boolean loggedIn = false;
    private static Uri profilePicAfterLoggedIn;
    private static String usernameAfterLoggedIn = "";
    private static int userIDAfterLoggedIn;

    private CustomListAdapter customListAdapter;
    private ArrayList<CustomRow> listViewOptions;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        application = requireActivity().getApplication();
        profilePicImgV = requireView().findViewById(R.id.profile_pic_imgv);
        loginBtn = requireView().findViewById(R.id.login_btn);
        profileOptionsListView = requireView().findViewById(R.id.profile_options_listview);

        setChangeProfilePicOnClick();
        setLoginBtnLoginOnClick();
        setProfileListViewOptions();
    }

    private void setProfileListViewOptions() {
        listViewOptions = new ArrayList<>();
        customListAdapter = new CustomListAdapter(requireActivity().getApplicationContext(), 0, listViewOptions);
        profileOptionsListView.setAdapter(customListAdapter);
        setMainListViewOptions();
        profileOptionsListView.setOnItemClickListener((parent, view1, position, id) -> {
            switch (listViewOptions.get(position).getId()) {
                case 0: //Settings
                    //TODO: Show Settings list.(Current confirmed options: logout.)
                    Log.d("ProfileOptionsListViewClick", "Settings clicked!");
                    listViewOptions.clear();
                    listViewOptions.add(new CustomRow(3, "Logout", R.drawable.ic_exit_foreground));
                    customListAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    //TODO: Show MyOrders activity.
                    break;
                case 2:
                    //TODO: Show OnMyShelf activity.
                    break;
                case 3: //Logout button, only appears when "Settings" clicked.
                    setMainListViewOptions();
                    break;
            }
        });
    }

    private void setMainListViewOptions() {
        listViewOptions.clear();
        listViewOptions.add(new CustomRow(0, "Settings", R.drawable.settings));
        listViewOptions.add(new CustomRow(1, "My Orders", R.drawable.shopping_bag));
        listViewOptions.add(new CustomRow(2, "On my Shelf", R.drawable.shelf));
        customListAdapter.notifyDataSetChanged();
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

    private final ActivityResultLauncher<Intent> selectProfileImgResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImg = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImg);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            // In case you want to compress your image, here it's at 40%
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            String imageToString = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                                    .create(UserViewModel.class);
                            userViewModel.updateUserProfilePic(application, userIDAfterLoggedIn, imageToString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                            userIDAfterLoggedIn = data.getIntExtra("UserID", 0);
                            deleteLoginBtn();
                        } else {
                            Toast.makeText(getActivity(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
}