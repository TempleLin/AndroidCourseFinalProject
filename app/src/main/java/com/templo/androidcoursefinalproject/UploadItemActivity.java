package com.templo.androidcoursefinalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.templo.androidcoursefinalproject.room_database.model.CategoryViewModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class UploadItemActivity extends AppCompatActivity {

    private Button chooseLocationBtn;
    private TextView itemLocShowTV;
    private Spinner itemCategorySpinner;
    private LinearLayout uploadImgsLinearLayout;
    private ArrayList<ImageView> allUploadImageVs = new ArrayList<>();
    private ArrayList<String> allUploadImageString = new ArrayList<>();

    private int maxAcceptableUploadImages = 5;
//    private int screenDPHeight;
//    float screenDPWidth;

    private int selectImageVIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

//        getScreenWidthHeightDP();

        itemLocShowTV = findViewById(R.id.itemLocShowTV);
        chooseLocationBtn = findViewById(R.id.itemChooseLocBtn);
        itemCategorySpinner = findViewById(R.id.itemCatergorySpinner);
        uploadImgsLinearLayout = findViewById(R.id.imageViewsLinearLayout);
        ImageView firstUploadImageV = findViewById(R.id.uploadImageV);
        firstUploadImageV.setOnClickListener(uploadImageViewOnClick);
//        firstUploadImageV.getLayoutParams().width = getViewDPFromPixels(firstUploadImageV, (int)screenDPWidth / 5);
        allUploadImageVs.add(firstUploadImageV);

        chooseLocationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsSelectLocActivity.class);
            mapSelectActivityResultLauncher.launch(intent);
        });

        //Set categories retrieved from categories table to spinner.
        CategoryViewModel categoryViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            ArrayList<String> arraySpinner = new ArrayList<>();
            categories.forEach(category -> {
                arraySpinner.add(category.getName());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadItemActivity.this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arraySpinner);
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                itemCategorySpinner.setAdapter(adapter);
            });
        });
    }

    private int getViewDPFromPixels(View view, int pixels) {
        float factor = view.getContext().getResources().getDisplayMetrics().density;
        return (int)(pixels * factor);
    }

//    private void getScreenWidthHeightDP() {
//        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
//        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
//        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//    }

    private final ActivityResultLauncher<Intent> mapSelectActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("ADDRESS", "MapsSelectLocActivity end.");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String address = data.getStringExtra("Address");
                        String featureName = data.getStringExtra("FeatureName");
                        String locality = data.getStringExtra("Locality");
                        String state = data.getStringExtra("State");
                        String subAdmin = data.getStringExtra("SubAdmin");
                        Log.d("ADDRESS", address);
                        Log.d("ADDRESS", featureName);
                        Log.d("ADDRESS", locality);
                        if (state != null) Log.d("ADDRESS", state);
                        if (subAdmin != null) Log.d("ADDRESS", subAdmin);

                        StringBuilder locResultBuilder = new StringBuilder();
                        if (state != null) locResultBuilder.append(state);
                        if (subAdmin != null) locResultBuilder.append(subAdmin);
                        if (locality != null) locResultBuilder.append(locality);

                        itemLocShowTV.setText(locResultBuilder.toString());
                    }
                } else {
                    Log.d("ADDRESS", "MapsSelectLocActivity failed.");
                }
            });

    private final View.OnClickListener uploadImageViewOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            AtomicInteger index = new AtomicInteger(0);
            allUploadImageVs.forEach(uploadImageV -> {
                if (uploadImageV == v) {
                    return;
                }
                index.getAndIncrement();
            });
            Log.d("INDEXBEFORE", String.valueOf(index.intValue()));
            selectImageVIndex = index.intValue();
            uploadImgResultLauncher.launch(intent);
        }
    };

    private final ActivityResultLauncher<Intent> uploadImgResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImg = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImg);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            // In case you want to compress your image, here it's at 40%
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            String imageToString = Base64.encodeToString(byteArray, Base64.DEFAULT);

                            int imageViewIndex = selectImageVIndex;
                            if (imageViewIndex + 1 > allUploadImageString.size()) {
                                allUploadImageString.add(imageToString);
                                if (allUploadImageString.size() < maxAcceptableUploadImages) {
                                    ImageView newImageView = new ImageView(UploadItemActivity.this);
                                    newImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                    newImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_foreground, null));
                                    newImageView.setOnClickListener(uploadImageViewOnClick);
                                    uploadImgsLinearLayout.addView(newImageView);
                                    allUploadImageVs.add(newImageView);
                                }
                            } else {
                                allUploadImageString.set(imageViewIndex, imageToString);
                            }
                            allUploadImageVs.get(imageViewIndex).setImageBitmap(bitmap);
                            Log.d("INDEX", String.valueOf(imageViewIndex));
//                            UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory(application)
//                                    .create(UserViewModel.class);
//                            TheDatabase.databaseWriteExecutor.execute(() -> {
//                                userViewModel.updateUserProfilePic(application, userIDAfterLoggedIn, imageToString);
//                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
}