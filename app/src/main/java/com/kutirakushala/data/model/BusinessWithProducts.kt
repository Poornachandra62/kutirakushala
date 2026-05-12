package com.kutirakushala.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class BusinessWithProducts(
    @Embedded val business: Business,
    @Relation(
        parentColumn = "id",
        entityColumn = "businessId"
    )
    val products: List<Product>
)
