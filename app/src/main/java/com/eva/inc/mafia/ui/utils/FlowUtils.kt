package com.eva.inc.mafia.ui.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.collectWithLifecycle(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.RESUMED,
    action: suspend (T) -> Unit,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state) {
            flow.collect(action)
        }
    }
}
