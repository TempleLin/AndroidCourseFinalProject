package com.templo.androidcoursefinalproject.room_database.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.templo.androidcoursefinalproject.room_database.model.CategoryWithProducts;
import com.templo.androidcoursefinalproject.room_database.model.UserWithProducts;
import com.templo.androidcoursefinalproject.room_database.util.TheDatabase;

import java.util.List;

import lombok.Getter;

public class RelationalRepository {
    private RelationalDAO relationalDAO;

    @Getter private LiveData<List<UserWithProducts>> allUsersWithProducts;
    @Getter private LiveData<List<CategoryWithProducts>> allCategoriesWithProducts;

    public RelationalRepository(Application application){
        TheDatabase db = TheDatabase.getDatabase(application);
        allUsersWithProducts = db.relationalDAO().getAllUsersWithProducts();
        allCategoriesWithProducts = db.relationalDAO().getAllCategoriesWithProducts();
    }
}
