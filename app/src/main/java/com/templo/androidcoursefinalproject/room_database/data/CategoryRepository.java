package com.templo.androidcoursefinalproject.room_database.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.templo.androidcoursefinalproject.room_database.model.Category;
import com.templo.androidcoursefinalproject.room_database.util.TheDatabase;

import java.util.List;

public class CategoryRepository {
    private CategoryDAO categoryDAO;
    private LiveData<List<Category>> allCategories;

    public CategoryRepository(Application application) {
        TheDatabase db = TheDatabase.getDatabase(application);
        categoryDAO = db.categoryDAO();
        allCategories = categoryDAO.getAllCategories();
    }

    public LiveData<List<Category>> getAllData() {return allCategories;}

    public void insert(Category category) {
        TheDatabase.databaseWriteExecutor.execute(() -> {
            categoryDAO.insert(category);
        });
    }

    public LiveData<Category> getCategory(Application application, String name) {
        TheDatabase db = TheDatabase.getDatabase(application);
        return db.categoryDAO().getCategory(name);
    }

    public LiveData<Boolean> categoryExists(Application application, String name) {
        TheDatabase db = TheDatabase.getDatabase(application);
        return db.categoryDAO().categoryExists(name);
    }
}
