package com.eva.inc.mafia.ui.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    val role: Role,
    val numbers: MutableList<Player?>,
) : Parcelable
