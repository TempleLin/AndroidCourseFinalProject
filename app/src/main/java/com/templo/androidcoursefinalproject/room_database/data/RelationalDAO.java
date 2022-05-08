package com.templo.androidcoursefinalproject.room_database.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.templo.androidcoursefinalproject.room_database.model.CategoryWithProducts;
import com.templo.androidcoursefinalproject.room_database.model.UserWithProducts;

import java.util.List;

@Dao
public interface RelationalDAO {
//    @Query("SELECT * FROM category_table JOIN product_table ON product_table.category = category_table.id WHERE category_table.id=:categoryId")
//    LiveData<CategoryWithProducts> getCategoryWithProducts(int categoryId);
//
//    @Query("SELECT * FROM user_table JOIN product_table ON product_table.sellerUserID = user_table.id WHERE user_table.id=:userId")
//    LiveData<UserWithProducts> getUserWithProducts(int userId);

    @Query("SELECT * FROM category_table JOIN product_table ON product_table.category = category_table.id")
    LiveData<List<CategoryWithProducts>> getAllCategoriesWithProducts();

    @Query("SELECT * FROM user_table JOIN product_table ON product_table.sellerUserID = user_table.id")
    LiveData<List<UserWithProducts>> getAllUsersWithProducts();
}
