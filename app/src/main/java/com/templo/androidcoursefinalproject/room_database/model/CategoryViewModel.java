package com.templo.androidcoursefinalproject.room_database.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.templo.androidcoursefinalproject.room_database.data.CategoryRepository;
import com.templo.androidcoursefinalproject.room_database.data.ProductRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    public static CategoryRepository repository;
    public final LiveData<List<Category>> allCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new CategoryRepository(application);
        allCategories = repository.getAllData();
    }

    public LiveData<List<Category>> getAllCategories() {return allCategories;}

    public static void insert(Application application, Category category) {
        if (repository == null) {
            new ViewModelProvider.AndroidViewModelFactory(application)
                    .create(CategoryViewModel.class);
        }
        repository.insert(category);
    }

    public LiveData<Category> getCategory(Application application, String name) {
        return repository.getCategory(application, name);
    }

    public LiveData<Boolean> categoryExists(Application application, String name) {
        return repository.categoryExists(application, name);
    }
}
