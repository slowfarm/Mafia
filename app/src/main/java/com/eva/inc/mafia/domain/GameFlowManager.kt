package com.eva.inc.mafia.domain

import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.entity.Moves
import com.eva.inc.mafia.ui.entity.Player
import com.eva.inc.mafia.ui.entity.Role

class GameFlowManager(
    private val domainRepository: DomainRepository = DomainRepository,
) {
    private val movesList = mutableListOf<Moves>()
    private var phaseState = PhaseState.ROLE_ASSIGNMENT
    private var lastWillStartIndex = -1

    private var wasNightPhase = false
    private var currentDayTurnIndex = 0
    private var currentRoleAssignmentIndex = 0
    private var isNewDayTurn = true

    fun startGame(): Moves? {
        resetState()
        movesList.clear()
        return nextSingleMove()
    }

    private fun resetState() {
        phaseState = PhaseState.ROLE_ASSIGNMENT
        domainRepository.pendingPlayers.clear()
        wasNightPhase = false
        currentDayTurnIndex = 0
        currentRoleAssignmentIndex = 0
        isNewDayTurn = true
        lastWillStartIndex = -1
    }

    fun nextSingleMove(): Moves? {
        val currentPlayers = domainRepository.players.value
        val allPlayers = domainRepository.allPlayers

        if (phaseState == PhaseState.END) return null

        val move =
            when (phaseState) {
                PhaseState.ROLE_ASSIGNMENT -> nextRoleAssignmentMove(currentPlayers)
                PhaseState.MAFIA_MEETUP ->
                    transitionMove(Moves.MafiaMeetUp, PhaseState.DAY_TURN) {
                        currentDayTurnIndex = 0
                        wasNightPhase = false
                        isNewDayTurn = true
                    }
                PhaseState.DAY_TURN -> nextDayTurnMove(currentPlayers)
                PhaseState.VOTE -> {
                    val move = Moves.Vote
                    movesList.add(move)
                    phaseState = PhaseState.CHECK_WIN
                    move
                }
                PhaseState.LAST_WILL -> nextLastWillMove()
                PhaseState.CHECK_WIN -> nextCheckWinMove(currentPlayers)
                PhaseState.NIGHT_MAFIA ->
                    transitionMove(
                        Moves.NightAction(Role.MAFIA),
                        PhaseState.NIGHT_DON,
                    ) {
                        wasNightPhase = true
                    }
                PhaseState.NIGHT_DON ->
                    nextNightRoleMove(
                        allPlayers,
                        Role.DON,
                        PhaseState.NIGHT_SHERIFF,
                    )
                PhaseState.NIGHT_SHERIFF ->
                    nextNightRoleMove(
                        allPlayers,
                        Role.SHERIFF,
                        PhaseState.NIGHT_DOCTOR,
                    )
                PhaseState.NIGHT_DOCTOR ->
                    nextNightRoleMove(
                        allPlayers,
                        Role.DOCTOR,
                        PhaseState.CHECK_WIN,
                    )
                PhaseState.END -> null
            }

        return move ?: nextSingleMove()
    }

    private fun nextRoleAssignmentMove(players: List<Player>): Moves? =
        if (currentRoleAssignmentIndex < players.size) {
            val move = Moves.RoleAssignment(players[currentRoleAssignmentIndex])
            currentRoleAssignmentIndex++
            if (currentRoleAssignmentIndex >= players.size) {
                phaseState = PhaseState.MAFIA_MEETUP
            }
            move
        } else {
            phaseState = PhaseState.MAFIA_MEETUP
            null
        }

    private fun nextDayTurnMove(players: List<Player>): Moves? {
        if (isNewDayTurn) {
            currentDayTurnIndex = 0
            isNewDayTurn = false
        }
        return if (currentDayTurnIndex < players.size) {
            val move = Moves.DayTurn(players[currentDayTurnIndex])
            currentDayTurnIndex++
            if (currentDayTurnIndex >= players.size) {
                phaseState = PhaseState.VOTE
            }
            move
        } else {
            phaseState = PhaseState.VOTE
            null
        }
    }

    private fun nextLastWillMove(): Moves? =
        if (domainRepository.pendingPlayers.isNotEmpty() && lastWillStartIndex >= 0 && lastWillStartIndex <= movesList.size) {
            val showed =
                movesList.subList(lastWillStartIndex, movesList.size).count { it is Moves.LastWill }
            if (showed < domainRepository.pendingPlayers.size) {
                val move = Moves.LastWill(domainRepository.pendingPlayers[showed])
                if (showed == domainRepository.pendingPlayers.size - 1) {
                    domainRepository.removePendingPlayers()
                    domainRepository.pendingPlayers.clear()
                    phaseState = PhaseState.CHECK_WIN
                    lastWillStartIndex = -1
                }
                movesList.add(move)
                move
            } else {
                phaseState = PhaseState.CHECK_WIN
                lastWillStartIndex = -1
                null
            }
        } else {
            phaseState = PhaseState.CHECK_WIN
            lastWillStartIndex = -1
            null
        }

    private fun nextCheckWinMove(players: List<Player>): Moves? {
        if (domainRepository.pendingPlayers.isNotEmpty()) {
            lastWillStartIndex = movesList.size
            phaseState = PhaseState.LAST_WILL
            return null
        }
        return if (getWinCondition(players)) {
            phaseState = PhaseState.END
            Moves.EndGame
        } else {
            if (wasNightPhase) {
                wasNightPhase = false
                isNewDayTurn = true
                phaseState = PhaseState.DAY_TURN
            } else {
                wasNightPhase = true
                phaseState = PhaseState.NIGHT_MAFIA
            }
            null
        }
    }

    private fun nextNightRoleMove(
        players: List<Player>,
        role: Role,
        nextPhase: PhaseState,
    ): Moves? =
        if (players.any { it.role == role }) {
            val move = Moves.NightAction(role)
            phaseState = nextPhase
            move
        } else {
            phaseState = nextPhase
            null
        }

    private fun transitionMove(
        move: Moves,
        nextPhase: PhaseState,
        onTransition: () -> Unit,
    ): Moves {
        onTransition()
        movesList.add(move)
        phaseState = nextPhase
        return move
    }

    private fun getWinCondition(players: List<Player>): Boolean {
        val mafiaCount = players.count { it.role == Role.MAFIA || it.role == Role.DON }
        val citizenCount = players.count { it.role != Role.MAFIA && it.role != Role.DON }
        return mafiaCount == 0 || mafiaCount >= citizenCount
    }

    private enum class PhaseState {
        ROLE_ASSIGNMENT,
        MAFIA_MEETUP,
        DAY_TURN,
        VOTE,
        LAST_WILL,
        CHECK_WIN,
        NIGHT_MAFIA,
        NIGHT_DON,
        NIGHT_SHERIFF,
        NIGHT_DOCTOR,
        END,
    }
}
