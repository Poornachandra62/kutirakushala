package com.kutirakushala.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kutirakushala.data.model.Business
import com.kutirakushala.data.model.Product

@Database(
    entities = [Business::class, Product::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KutiraKushalaDatabase : RoomDatabase() {

    abstract fun businessDao(): BusinessDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: KutiraKushalaDatabase? = null

        fun getDatabase(context: Context): KutiraKushalaDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    KutiraKushalaDatabase::class.java,
                    "kutira_kushala_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
