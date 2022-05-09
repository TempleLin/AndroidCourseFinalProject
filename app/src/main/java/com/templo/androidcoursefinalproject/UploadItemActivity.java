package com.templo.androidcoursefinalproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
    private ArrayList<ImageView> allUploadImageVs = new ArrayList<>();
    private ArrayList<String> allUploadImageString = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        itemLocShowTV = findViewById(R.id.itemLocShowTV);
        chooseLocationBtn = findViewById(R.id.itemChooseLocBtn);
        itemCategorySpinner = findViewById(R.id.itemCatergorySpinner);
        ImageView firstUploadImageV = findViewById(R.id.uploadImageV);
        firstUploadImageV.setOnClickListener(uploadImageViewOnClick);
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
            intent.putExtra("Index", index.intValue());
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

                            int imageViewIndex = data.getIntExtra("Index", 0);
                            if (imageViewIndex + 1 > allUploadImageString.size()) {
                                allUploadImageString.add(imageToString);
                            } else {
                                allUploadImageString.set(imageViewIndex, imageToString);
                            }
                            allUploadImageVs.get(imageViewIndex).setImageBitmap(bitmap);

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