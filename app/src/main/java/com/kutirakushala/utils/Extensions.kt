package com.kutirakushala.utils

import android.content.Context
import android.widget.Toast
import com.kutirakushala.data.model.CapacityStatus
import com.kutirakushala.data.model.ProductCategory

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun CapacityStatus.displayName(): String = when (this) {
    CapacityStatus.AVAILABLE -> "Available"
    CapacityStatus.FULL -> "Full"
    CapacityStatus.PAUSED -> "Paused"
}

fun CapacityStatus.colorRes(): Int = when (this) {
    CapacityStatus.AVAILABLE -> android.R.color.holo_green_dark
    CapacityStatus.FULL -> android.R.color.holo_red_dark
    CapacityStatus.PAUSED -> android.R.color.darker_gray
}

fun String.toCapacityStatus(): CapacityStatus =
    CapacityStatus.entries.firstOrNull { it.name == this } ?: CapacityStatus.AVAILABLE

fun String.toCategoryDisplayName(): String =
    ProductCategory.entries.firstOrNull { it.name == this }?.displayName ?: this
