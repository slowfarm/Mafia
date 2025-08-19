package com.eva.inc.mafia.ui.utils

import android.os.Build
import android.os.Bundle

fun <T> lazyUi(block: () -> T): Lazy<T> = lazy(mode = LazyThreadSafetyMode.NONE, initializer = block)

inline fun <reified T : android.os.Parcelable> Bundle?.getParcelableCompat(key: String): T =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this?.getParcelable(key, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        this?.getParcelable(key)
    } ?: throw IllegalStateException("No value for key \"$key\" in bundle")
