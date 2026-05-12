package com.kutirakushala

import android.app.Application
import com.kutirakushala.data.db.KutiraKushalaDatabase
import com.kutirakushala.data.repository.KutiraKushalaRepository

class KutiraKushalaApp : Application() {
    val database by lazy { KutiraKushalaDatabase.getDatabase(this) }
    val repository by lazy {
        KutiraKushalaRepository(database.businessDao(), database.productDao())
    }
}
