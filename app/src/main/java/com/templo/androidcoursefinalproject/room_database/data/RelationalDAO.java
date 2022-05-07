package com.templo.androidcoursefinalproject.room_database.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.templo.androidcoursefinalproject.room_database.model.CategoryWithProducts;

import java.util.List;

@Dao
public interface RelationalDAO {
    @Query("SELECT * FROM category_table JOIN product_table ON product_table.category = category_table.id")
    LiveData<List<CategoryWithProducts>> getAllCategoryWithProducts();
}
