package com.eva.inc.mafia.ui.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Move : Parcelable {
    @Parcelize
    data class RoleAssignment(
        val player: Player,
    ) : Move()

    @Parcelize
    object MafiaMeetUp : Move()

    @Parcelize
    data class DayTurn(
        val player: Player,
    ) : Move()

    @Parcelize
    object Vote : Move()

    @Parcelize
    data class NightAction(
        val role: Role,
    ) : Move()

    @Parcelize
    data class LastWill(
        val player: Player,
    ) : Move()

    @Parcelize
    object EndGame : Move()
}
