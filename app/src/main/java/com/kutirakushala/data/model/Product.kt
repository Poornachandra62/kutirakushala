package com.kutirakushala.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Business::class,
            parentColumns = ["id"],
            childColumns = ["businessId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("businessId")]
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val businessId: Int = 0,
    val name: String = "",
    val unit: String = "",               // e.g. "per piece", "per kg"
    val wholesalePrice: Double = 0.0,
    val minimumOrder: Int = 1,
    val photoUri: String = "",
    val description: String = ""
)
