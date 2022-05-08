package com.templo.androidcoursefinalproject.room_database.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.templo.androidcoursefinalproject.room_database.data.RelationalRepository;

import java.util.List;

import lombok.Getter;

public class RelationalViewModel extends AndroidViewModel {
    public static RelationalRepository repository;
    public static RelationalViewModel instance;
    public final LiveData<List<UserWithProducts>> allUsersWithProducts;
    public final LiveData<List<CategoryWithProducts>> allCategoriesWithProducts;

    public RelationalViewModel(@NonNull Application application) {
        super(application);
        repository = new RelationalRepository(application);
        allUsersWithProducts = repository.getAllUsersWithProducts();
        allCategoriesWithProducts = repository.getAllCategoriesWithProducts();
    }

    public LiveData<List<UserWithProducts>> getAllUsersWithProducts(Application application) {
        return allUsersWithProducts;
    }

    public LiveData<List<CategoryWithProducts>> getAllCategoriesWithProducts(Application application) {
        return allCategoriesWithProducts;
    }
}
