package com.templo.androidcoursefinalproject.room_database.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.templo.androidcoursefinalproject.room_database.model.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE) //Ignore if conflict happens.
    void insert(Product product);

    @Query("DELETE FROM product_table") //"product_table" gets auto-recognized by intellisense.
    void deleteAll();

    @Query("SELECT * FROM product_table ORDER BY productName ASC") //ASC means ascending.
    LiveData<List<Product>> getAllProducts(); //Data is encapsulated by LiveData.

    @Query("SELECT * FROM product_table WHERE productName=:productName AND category=:category " +
            "AND location=:location")
    LiveData<Product> getProduct(String productName, int category, String location);

    @Query("SELECT * FROM product_table WHERE id=:id")
    LiveData<Product> getProduct(int id);

    @Query("SELECT EXISTS(SELECT * FROM product_table WHERE productName=:productName AND category=:category)")
    LiveData<Boolean> productExists(String productName, int category);
}
