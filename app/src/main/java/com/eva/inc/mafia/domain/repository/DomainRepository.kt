package com.eva.inc.mafia.domain.repository

import android.content.Context
import androidx.core.content.edit
import com.eva.inc.mafia.ui.entity.GameSnapshot
import com.eva.inc.mafia.ui.entity.Player
import com.eva.inc.mafia.ui.entity.Role
import com.eva.inc.mafia.ui.entity.Step
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DomainRepository(
    private val context: Context,
) {
    private val gson = Gson()

    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _exhibitedPlayers = MutableStateFlow<Set<Player>>(emptySet())
    val exhibitedPlayers: StateFlow<Set<Player>> = _exhibitedPlayers.asStateFlow()

    var pendingPlayers = mutableSetOf<Player>()

    var allPlayers = emptyList<Player>()
        private set

    var steps = mutableListOf<Step>()

    fun setPlayers(players: List<Player>) {
        val roles = Player.Companion.generate(players.size)
        val playersWithRoles = players.zip(roles).map { (player, role) -> player.copy(role = role) }
        _players.value = playersWithRoles
        allPlayers = playersWithRoles
        resetExhibited()
    }

    fun addExhibitedPlayer(player: Player) {
        _exhibitedPlayers.value = _exhibitedPlayers.value + player
    }

    fun removePendingPlayers(player: Player) {
        _players.value = _players.value - player
        _exhibitedPlayers.value = _exhibitedPlayers.value - player
        pendingPlayers.remove(player)
    }

    fun resetExhibited() {
        _exhibitedPlayers.value = emptySet()
    }

    fun addSnapshot(snapshot: GameSnapshot) {
        saveSnapshotToPrefs(snapshot)
    }

    fun addStep(
        role: Role,
        player: Player?,
    ) {
        val roleStep = steps.firstOrNull { it.role == role }
        val moves = roleStep?.numbers?.toMutableList() ?: mutableListOf()
        moves.add(player)
        if (roleStep == null) {
            steps.add(Step(role, moves))
        } else {
            steps =
                steps
                    .map {
                        if (it.role == role) {
                            Step(role, moves)
                        } else {
                            it
                        }
                    }.toMutableList()
        }
    }

    fun saveSnapshotToPrefs(snapshot: GameSnapshot?) {
        val json = snapshot?.let { serializeSnapshots(it) }
        getSharedPreferences().edit { putString(KEY_SNAPSHOTS, json) }
    }

    fun loadSnapshotFromPrefs(): GameSnapshot? {
        val json = getSharedPreferences().getString(KEY_SNAPSHOTS, null)
        return json?.let { deserializeSnapshots(it) }
    }

    private fun getSharedPreferences() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun serializeSnapshots(snapshots: GameSnapshot): String = gson.toJson(snapshots)

    private fun deserializeSnapshots(json: String): GameSnapshot? = gson.fromJson(json, GameSnapshot::class.java)

    fun restore(snapshot: GameSnapshot) {
        _players.value = snapshot.players
        _exhibitedPlayers.value = snapshot.exhibitedPlayers.toSet()
        pendingPlayers = snapshot.pendingPlayers.toMutableSet()
        allPlayers = snapshot.allPlayers
        steps = snapshot.steps.toMutableList()
    }

    companion object {
        private const val PREFS_NAME = "game_prefs"
        private const val KEY_SNAPSHOTS = "snapshots"
    }
}
