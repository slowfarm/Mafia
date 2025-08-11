package com.eva.inc.mafia.ui.utils

fun <T> lazyUi(block: () -> T): Lazy<T> =
    lazy(mode = LazyThreadSafetyMode.NONE, initializer = block)