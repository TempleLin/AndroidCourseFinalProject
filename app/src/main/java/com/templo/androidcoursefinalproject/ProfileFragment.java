package com.templo.androidcoursefinalproject;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
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
import android.view.Gravity;
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

import com.google.android.material.snackbar.Snackbar;
import com.templo.androidcoursefinalproject.custom_list.*;
import com.templo.androidcoursefinalproject.custom_list.CustomRow;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;
import com.templo.androidcoursefinalproject.room_database.util.UserRoomDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Notes:
 *  1.Profile picture's round bc its parent cardview's cornerRadius is set to 100dp.
 */
public class ProfileFragment extends Fragment {
    private Application application;

    private ImageView profilePicImgV;
    private Button loginBtn;
    private ListView profileOptionsListView;

    private TextView showUsernameTextView; //This only gets value assigned when logged in.

    private static boolean loggedIn = false;
    private static Bitmap profilePicAfterLoggedIn;
    private static String usernameAfterLoggedIn = "";

    public final static int USER_NOT_LOGIN_ID = -100;
    public final static String USER_NOT_LOGIN_NAME = "";

    private static int userIDAfterLoggedIn = USER_NOT_LOGIN_ID;

    private CustomListAdapter customListAdapter;
    private ArrayList<CustomRow> listViewOptions;

    ProfileListViewEdits profileListViewEdits;

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

        profileListViewEdits = new ProfileListViewEdits(customListAdapter, listViewOptions);

        profileOptionsListView.setAdapter(customListAdapter);
        profileListViewEdits.setMainListViewOptions();
        profileOptionsListView.setOnItemClickListener((parent, view1, position, id) -> {
            switch (listViewOptions.get(position).getId()) {
                case ProfileListViewEdits.SETTINGS_ID: //Settings
                    //TODO: Show Settings list.(Current confirmed options: logout.)
                    Log.d("ProfileOptionsListViewClick", "Settings clicked!");
                    listViewOptions.clear();
                    if (loggedIn) {
                        listViewOptions.add(new CustomRow(ProfileListViewEdits.LOGOUT_ID, "Logout", R.drawable.ic_exit_foreground));
                    }
                    listViewOptions.add(new CustomRow(ProfileListViewEdits.BACK_ID, "Back", R.drawable.ic_arrow_back_foreground));
                    customListAdapter.notifyDataSetChanged();
                    break;
                case ProfileListViewEdits.MY_ORDERS_ID:
                    //TODO: Show MyOrders activity.
                    break;
                case ProfileListViewEdits.ON_MY_SHELF_ID:
                    listViewOptions.clear();
                    listViewOptions.add(new CustomRow(ProfileListViewEdits.MY_SELLING_ITEMS_ID, "My Selling Items", R.drawable.ic_money_foreground));
                    listViewOptions.add(new CustomRow(ProfileListViewEdits.UPLOAD_ITEM_ID, "Upload an Item", R.drawable.ic_upload_foreground));
                    listViewOptions.add(new CustomRow(ProfileListViewEdits.BACK_ID, "Back", R.drawable.ic_arrow_back_foreground));
                    customListAdapter.notifyDataSetChanged();
                    break;
                case ProfileListViewEdits.LOGOUT_ID: //Logout button, only appears when "Settings" clicked.
                    profileListViewEdits.setMainListViewOptions();
                    removeUserDetailsForLogout();
                    deleteUserName_showLoginBtn();
                    clearImageProfilePicView();
                    loggedIn = false;
                    break;
                case ProfileListViewEdits.BACK_ID: //Back button, appears when "Settings" clicked.
                    profileListViewEdits.setMainListViewOptions();
                    break;
                case ProfileListViewEdits.MY_SELLING_ITEMS_ID: //TODO: Design and show My Selling Items activity.
                    break;
                case ProfileListViewEdits.UPLOAD_ITEM_ID:
                    Intent uploadItemIntent = new Intent(requireActivity(), UploadItemActivity.class);
                    uploadItemResultLauncher.launch(uploadItemIntent);
                    break;
            }
        });
    }

    private static class ProfileListViewEdits {
        private final CustomListAdapter customListAdapter;
        private final ArrayList<CustomRow> listViewOptions;

        public ProfileListViewEdits(CustomListAdapter customListAdapter, ArrayList<CustomRow> listViewOptions) {
            this.customListAdapter = customListAdapter;
            this.listViewOptions = listViewOptions;
        }
        public void setMainListViewOptions() {
            listViewOptions.clear();
            listViewOptions.add(new CustomRow(SETTINGS_ID, "Settings", R.drawable.settings));
            listViewOptions.add(new CustomRow(MY_ORDERS_ID, "My Orders", R.drawable.shopping_bag));
            listViewOptions.add(new CustomRow(ON_MY_SHELF_ID, "On my Shelf", R.drawable.shelf));
            customListAdapter.notifyDataSetChanged();
        }

        //IDs for listview item id.
        public final static int SETTINGS_ID = 0;
        public final static int MY_ORDERS_ID = 1;
        public final static int ON_MY_SHELF_ID = 2;
        public final static int LOGOUT_ID = 3;
        public final static int BACK_ID = 4;
        public final static int MY_SELLING_ITEMS_ID = 5;
        public final static int UPLOAD_ITEM_ID = 6;
    }

    private void setLoginBtnLoginOnClick() {
        if (loggedIn) {
            deleteLoginBtn_showUsername();
            profilePicImgV.setImageBitmap(profilePicAfterLoggedIn);
            return;
        }
        loginBtn.setOnClickListener(v -> {
            Intent loginIntent = new Intent(getActivity(), LoginRegisterActivity.class);
            loginResultLauncher.launch(loginIntent);
        });
    }

    private void setChangeProfilePicOnClick() {
        profilePicImgV.setOnClickListener(v -> {
            if (usernameAfterLoggedIn.equals(USER_NOT_LOGIN_NAME) && userIDAfterLoggedIn == USER_NOT_LOGIN_ID) {
                Snackbar.make(profilePicImgV, "User not logged in!", Snackbar.LENGTH_SHORT).show();
            } else {
                //Pick images from device. Tutorial reference: https://www.youtube.com/watch?v=H1ja8gvTtBE
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                selectProfileImgResultLauncher.launch(intent);
            }
        });
    }

    private void deleteUserName_showLoginBtn() {
        ViewGroup profileViewGroup = (ViewGroup) showUsernameTextView.getParent();
        if (profileViewGroup != null) {
            profileViewGroup.removeView(showUsernameTextView);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER_HORIZONTAL;
            loginBtn.setLayoutParams(params);
            profileViewGroup.addView(loginBtn);
        }
    }

    private void deleteLoginBtn_showUsername() {
        //Delete button after logged in. Tutorial reference: https://stackoverflow.com/questions/3995215/add-and-remove-views-in-android-dynamically
        ViewGroup parent = (ViewGroup) loginBtn.getParent();
        if (parent != null) {
            parent.removeView(loginBtn);
            showUsernameTextView = new TextView(getActivity());
            showUsernameTextView.setText(usernameAfterLoggedIn);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(64, 0, 0, 0);
            showUsernameTextView.setLayoutParams(params);
            showUsernameTextView.setTextSize(20);
            parent.addView(showUsernameTextView);
        }
    }

    private void clearImageProfilePicView() {
        profilePicImgV.setImageResource(R.mipmap.ic_launcher); //Clear image back to default.
        profilePicAfterLoggedIn = null;
    }

    private void setImageProfilePicView(Uri image) {
        profilePicImgV.setImageURI(image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try { //Save image to buffer.
                profilePicAfterLoggedIn = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().getContentResolver(), image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                profilePicAfterLoggedIn = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeUserDetailsForLogout() {
        usernameAfterLoggedIn = USER_NOT_LOGIN_NAME;
        userIDAfterLoggedIn = USER_NOT_LOGIN_ID;
    }

    private final ActivityResultLauncher<Intent> uploadItemResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Log.d("TAG", "Upload Item Result.");
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> selectProfileImgResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImg = data.getData();
                        setImageProfilePicView(selectedImg);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImg);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            // In case you want to compress your image, here it's at 40%
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            String imageToString = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                                    .create(UserViewModel.class);
                            UserRoomDatabase.databaseWriteExecutor.execute(() -> {
                                userViewModel.updateUserProfilePic(application, userIDAfterLoggedIn, imageToString);
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                            deleteLoginBtn_showUsername();

                            //Set profile pic.
                            byte[] encodeByte = Base64.decode(data.getStringExtra("ProfilePic"), Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            profilePicImgV.setImageBitmap(bitmap);
                            profilePicAfterLoggedIn = bitmap;
                        } else {
                            Toast.makeText(getActivity(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
}