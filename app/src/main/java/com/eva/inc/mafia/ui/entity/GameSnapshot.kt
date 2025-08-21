package com.eva.inc.mafia.ui.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSnapshot(
    val players: List<Player>,
    val exhibitedPlayers: List<Player>,
    val pendingPlayers: List<Player>,
    val allPlayers: List<Player>,
    val steps: List<Step>,
    val phaseState: String,
    val wasNightPhase: Boolean,
    val currentDayTurnIndex: Int,
    val currentRoleAssignmentIndex: Int,
    val isNewDayTurn: Boolean,
) : Parcelable
