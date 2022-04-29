package com.templo.androidcoursefinalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UploadItemActivity extends AppCompatActivity {

    private Button chooseLocationBtn;
    private TextView itemLocShowTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        itemLocShowTV = findViewById(R.id.itemLocShowTV);
        chooseLocationBtn = findViewById(R.id.itemChooseLocBtn);

        chooseLocationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsSelectLocActivity.class);
            activityResultLauncher.launch(intent);
        });
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("ADDRESS", "MapsSelectLocActivity end.");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String address = data.getStringExtra("Address");
                        String featureName = data.getStringExtra("FeatureName");
                        String city = data.getStringExtra("City");
                        Log.d("ADDRESS", address);
                        Log.d("ADDRESS", featureName);
                        Log.d("ADDRESS", city);
                        itemLocShowTV.setText(city);
                    }
                } else {
                    Log.d("ADDRESS", "MapsSelectLocActivity failed.");
                }
            });
}