package com.eva.inc.mafia.domain.repository

import com.eva.inc.mafia.ui.entity.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object DomainRepository {
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    private val _exhibitedPlayers = MutableStateFlow<Set<Player>>(emptySet())
    val exhibitedPlayers: StateFlow<Set<Player>> = _exhibitedPlayers.asStateFlow()

    var pendingPlayers = mutableSetOf<Player>()

    var allPlayers: List<Player> = emptyList()
        private set

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
}
