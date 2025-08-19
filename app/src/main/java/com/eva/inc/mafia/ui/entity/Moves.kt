package com.eva.inc.mafia.ui.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Moves : Parcelable {
    @Parcelize
    data class RoleAssignment(
        val player: Player,
    ) : Moves()

    @Parcelize
    object MafiaMeetUp : Moves()

    @Parcelize
    data class DayTurn(
        val player: Player,
    ) : Moves()

    @Parcelize
    object Vote : Moves()

    @Parcelize
    data class NightAction(
        val role: Role,
    ) : Moves()

    @Parcelize
    data class LastWill(
        val player: Player,
    ) : Moves()

    @Parcelize
    object EndGame : Moves()
}
