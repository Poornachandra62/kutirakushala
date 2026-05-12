package com.kutirakushala.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CapacityStatus { AVAILABLE, FULL, PAUSED }

enum class ProductCategory(val displayName: String) {
    FOOD("Food"),
    CRAFT("Craft"),
    TEXTILE("Textile"),
    HOME_CARE("Home Care"),
    JEWELLERY("Jewellery"),
    OTHER("Other")
}

@Entity(tableName = "businesses")
data class Business(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerName: String = "",
    val businessName: String = "",
    val skillArea: String = "",
    val location: String = "",           // village / taluk
    val description: String = "",
    val phoneNumber: String = "",
    val photoUri: String = "",           // local URI string
    val workspacePhotoUri: String = "",
    val category: String = ProductCategory.CRAFT.name,
    val capacityUnits: Int = 0,
    val capacityStatus: CapacityStatus = CapacityStatus.AVAILABLE,
    val createdAt: Long = System.currentTimeMillis()
)
