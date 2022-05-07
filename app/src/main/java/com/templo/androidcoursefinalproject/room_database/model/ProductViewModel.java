package com.templo.androidcoursefinalproject.room_database.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.templo.androidcoursefinalproject.room_database.data.ProductRepository;

import java.util.List;

/*
ViewModel holds all the data (LiveData) needed for the UI. UI is notified of changes using "observation".
 */
public class ProductViewModel extends AndroidViewModel {

    public static ProductRepository repository;
    public final LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepository(application);
        allProducts = repository.getAllData();
    }

    public LiveData<List<Product>> getAllProducts() { return allProducts; }
    public static void insert(Application application, Product product) {
        if (repository == null) {
            new ViewModelProvider.AndroidViewModelFactory(application)
                    .create(ProductViewModel.class);
        }
        repository.insert(product);
    }
//    public static void insertOnlyOne(Application application, Product product) { //Product must be unique.
//        if (repository.getProduct(application, product.getProductName(), product.getCategory(), product.getLocation())!= null) {
//            repository.insert(product);
//        }
//    }
    public LiveData<Product> getProduct(Application application, String productName, int category, String location) {
        return repository.getProduct(application, productName, category, location);
    }

    public LiveData<Boolean> productExists(Application application, String productName, int category, String location) {
        return repository.productExists(application, productName,category, location);
    }
}
