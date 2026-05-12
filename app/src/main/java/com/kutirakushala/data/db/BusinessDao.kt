package com.kutirakushala.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kutirakushala.data.model.Business
import com.kutirakushala.data.model.BusinessWithProducts
import com.kutirakushala.data.model.CapacityStatus

@Dao
interface BusinessDao {

    @Query("SELECT * FROM businesses ORDER BY createdAt DESC")
    fun getAllBusinesses(): LiveData<List<Business>>

    @Query("SELECT * FROM businesses ORDER BY createdAt DESC")
    suspend fun getAllBusinessesOnce(): List<Business>

    @Transaction
    @Query("SELECT * FROM businesses WHERE id = :id")
    fun getBusinessWithProducts(id: Int): LiveData<BusinessWithProducts>

    @Query("SELECT * FROM businesses WHERE id = :id")
    suspend fun getBusinessById(id: Int): Business?

    // Search by category
    @Query("SELECT * FROM businesses WHERE category = :category ORDER BY createdAt DESC")
    fun getByCategory(category: String): LiveData<List<Business>>

    // Search by location (case-insensitive LIKE)
    @Query("SELECT * FROM businesses WHERE LOWER(location) LIKE '%' || LOWER(:location) || '%' ORDER BY createdAt DESC")
    fun getByLocation(location: String): LiveData<List<Business>>

    // Search by category AND location
    @Query("""
        SELECT * FROM businesses 
        WHERE category = :category 
        AND LOWER(location) LIKE '%' || LOWER(:location) || '%'
        ORDER BY createdAt DESC
    """)
    fun getByCategoryAndLocation(category: String, location: String): LiveData<List<Business>>

    // Full-text search across name, skillArea, location
    @Query("""
        SELECT * FROM businesses
        WHERE LOWER(businessName) LIKE '%' || LOWER(:query) || '%'
        OR LOWER(skillArea) LIKE '%' || LOWER(:query) || '%'
        OR LOWER(location) LIKE '%' || LOWER(:query) || '%'
        ORDER BY createdAt DESC
    """)
    fun search(query: String): LiveData<List<Business>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBusiness(business: Business): Long

    @Update
    suspend fun updateBusiness(business: Business): Int

    @Delete
    suspend fun deleteBusiness(business: Business): Int

    @Query("UPDATE businesses SET capacityUnits = :units, capacityStatus = :status WHERE id = :id")
    suspend fun updateCapacity(id: Int, units: Int, status: CapacityStatus): Int

    @Query("SELECT COUNT(*) FROM businesses")
    suspend fun getCount(): Int
}
