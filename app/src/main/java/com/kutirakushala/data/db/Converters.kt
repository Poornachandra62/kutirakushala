package com.kutirakushala.data.db

import androidx.room.TypeConverter
import com.kutirakushala.data.model.CapacityStatus

class Converters {
    @TypeConverter
    fun fromCapacityStatus(value: CapacityStatus): String = value.name

    @TypeConverter
    fun toCapacityStatus(value: String): CapacityStatus =
        CapacityStatus.valueOf(value)
}
