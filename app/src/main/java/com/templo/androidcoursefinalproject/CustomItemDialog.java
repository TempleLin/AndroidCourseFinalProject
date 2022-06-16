package com.templo.androidcoursefinalproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.templo.androidcoursefinalproject.room_database.model.CategoryViewModel;
import com.templo.androidcoursefinalproject.room_database.model.Product;
import com.templo.androidcoursefinalproject.room_database.model.UserViewModel;

public class CustomItemDialog extends Dialog {
    private Product product;
    private Activity activity;

    public CustomItemDialog(@NonNull Context context) {
        super(context);
        activity = (Activity) context;
    }

    public CustomItemDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomItemDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_custom_dialog);
        getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg); //Make dialog have round corners.

        TextView productNameTV = findViewById(R.id.productName);
        TextView ownerName = findViewById(R.id.sellerName);
        ImageView productImg = findViewById(R.id.productImg);
        TextView category = findViewById(R.id.category);
        TextView location = findViewById(R.id.location);
        TextView description = findViewById(R.id.description);

        productNameTV.setText(String.format("Product: %s", product.getProductName()));
        Bitmap bm = stringToBitMap(product.getImage1());
        productImg.setImageBitmap(bm);
        location.setText(String.format("Loc: %s", product.getLocation()));
        description.setText(String.format("Description: %s", product.getDescription()));

//        ProductViewModel productViewModel = new ViewModelProvider.AndroidViewModelFactory(getOwnerActivity().getApplication())
//                .create(ProductViewModel.class);
        UserViewModel userViewModel = new ViewModelProvider.AndroidViewModelFactory(activity.getApplication())
                .create(UserViewModel.class);
        CategoryViewModel categoryViewModel = new ViewModelProvider.AndroidViewModelFactory(activity.getApplication())
                .create(CategoryViewModel.class);

        //This makes the code in run() execute in main thread. .observe() cannot work on background thread.
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            userViewModel.getUser(activity.getApplication(), product.getId()).observe((LifecycleOwner) activity, user -> {
                if (user != null) {
                    ownerName.setText(String.format("Owner: %s", user.getName()));
                } else {
                    ownerName.setText(R.string.owner_none);
                }
            });
            categoryViewModel.getCategory(activity.getApplication(), product.getCategory()).observe((LifecycleOwner) activity, _category -> {
                if (_category != null) {
                    category.setText(String.format("Category: %s", _category.getName()));
                } else {
                    category.setText(R.string.category_none);
                }
            });
        });
    }

    public CustomItemDialog setProduct(Product product) {
        this.product = product;
        return this;
    }

    private Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            Log.w("StringBitmapWarn", e.getMessage());
            return null;
        }
    }
}
