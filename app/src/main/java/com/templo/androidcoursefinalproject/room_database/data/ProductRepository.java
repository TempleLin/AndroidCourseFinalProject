package com.templo.androidcoursefinalproject.room_database.data;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.templo.androidcoursefinalproject.room_database.model.Product;
import com.templo.androidcoursefinalproject.room_database.model.User;
import com.templo.androidcoursefinalproject.room_database.util.TheDatabase;

import java.util.List;

public class ProductRepository {
    private ProductDAO productDao;
    private LiveData<List<Product>> allProducts; //Data is encapsulated by LiveData.

    public ProductRepository(Application application) {
        TheDatabase db = TheDatabase.getDatabase(application);
        productDao = db.productDAO();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllData() { return allProducts; }
    public void insert(Product product) {
        /*
        .execute(): Executes the given command at some time in the future. The command may execute in a new thread, in a pooled thread,
            or in the calling thread, at the discretion of the Executor implementation.
         */
        TheDatabase.databaseWriteExecutor.execute(() -> {
            //.insert must be used in the background thread, that's why it's in .execute callback.
            productDao.insert(product);
        });
    }
    public LiveData<Product> getProduct(Application application, String productName, int category, String location) {
        TheDatabase db = TheDatabase.getDatabase(application);
        return db.productDAO().getProduct(productName, category, location);
    }

    public LiveData<Boolean> productExists(Application application, String productName, int category, String location) {
        TheDatabase db = TheDatabase.getDatabase(application);
        return db.productDAO().productExists(productName, category);
    }
}