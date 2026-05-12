package com.kutirakushala.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kutirakushala.data.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM products WHERE businessId = :businessId")
    fun getProductsForBusiness(businessId: Int): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE businessId = :businessId")
    suspend fun getProductsForBusinessOnce(businessId: Int): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    @Update
    suspend fun updateProduct(product: Product): Int

    @Delete
    suspend fun deleteProduct(product: Product): Int

    @Query("DELETE FROM products WHERE businessId = :businessId")
    suspend fun deleteAllForBusiness(businessId: Int): Int

    @Query("SELECT COUNT(*) FROM products WHERE businessId = :businessId")
    suspend fun countForBusiness(businessId: Int): Int
}
