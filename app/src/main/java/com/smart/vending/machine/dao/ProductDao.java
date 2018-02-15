package com.smart.vending.machine.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.smart.vending.machine.entity.Product;
import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT COUNT(*) from product")
    int countProducts();

    @Insert
    void insertAll(Product... products);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT product_name FROM product ")
    List<String> getProductNamesList();

    @Update
    public void updateProducts(Product... products);
}