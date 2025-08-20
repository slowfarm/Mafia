package com.eva.inc.mafia.domain

import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.entity.Moves
import com.eva.inc.mafia.ui.entity.Role

class GameFlowManager(
    private val domainRepository: DomainRepository = DomainRepository,
) {
    private var phaseState = PhaseState.ROLE_ASSIGNMENT
    private var wasNightPhase = false
    private var currentDayTurnIndex = 0
    private var currentRoleAssignmentIndex = 0
    private var isNewDayTurn = true

    fun startGame(): Moves? {
        resetState()
        return nextSingleMove()
    }

    fun nextSingleMove(): Moves? {
        if (phaseState == PhaseState.END) return null

        val move =
            when (phaseState) {
                PhaseState.ROLE_ASSIGNMENT -> nextRoleAssignmentMove()
                PhaseState.MAFIA_MEETUP -> nextMafiaMeetup()
                PhaseState.DAY_TURN -> nextDayTurnMove()
                PhaseState.VOTE -> getMoveAndSetPhase(Moves.Vote, PhaseState.CHECK_WIN)
                PhaseState.LAST_WILL -> nextLastWillMove()
                PhaseState.CHECK_WIN -> nextCheckWinMove()
                PhaseState.NIGHT_MAFIA -> nextNightMafiaMove()
                PhaseState.NIGHT_DON -> nextNightRoleMove(Role.DON, PhaseState.NIGHT_SHERIFF)
                PhaseState.NIGHT_SHERIFF -> nextNightRoleMove(Role.SHERIFF, PhaseState.NIGHT_DOCTOR)
                PhaseState.NIGHT_DOCTOR -> nextNightRoleMove(Role.DOCTOR, PhaseState.CHECK_WIN)
                PhaseState.END -> null
            }

        return move ?: nextSingleMove()
    }

    private fun resetState() {
        phaseState = PhaseState.ROLE_ASSIGNMENT
        domainRepository.pendingPlayers.clear()
        wasNightPhase = false
        currentDayTurnIndex = 0
        currentRoleAssignmentIndex = 0
        isNewDayTurn = true
    }

    private fun nextMafiaMeetup(): Moves? {
        currentDayTurnIndex = 0
        wasNightPhase = false
        isNewDayTurn = true
        return getMoveAndSetPhase(Moves.MafiaMeetUp, PhaseState.DAY_TURN)
    }

    private fun nextRoleAssignmentMove(): Moves? {
        val players = domainRepository.players.value
        return if (currentRoleAssignmentIndex < players.size) {
            val move = Moves.RoleAssignment(players[currentRoleAssignmentIndex])
            currentRoleAssignmentIndex++
            getMoveAndSetPhase(
                move,
                PhaseState.MAFIA_MEETUP,
                currentRoleAssignmentIndex >= players.size,
            )
        } else {
            getMoveAndSetPhase(null, PhaseState.MAFIA_MEETUP)
        }
    }

    private fun nextDayTurnMove(): Moves? {
        if (isNewDayTurn) {
            currentDayTurnIndex = 0
            isNewDayTurn = false
        }
        val players = domainRepository.players.value
        return if (currentDayTurnIndex < players.size) {
            val move = Moves.DayTurn(players[currentDayTurnIndex])
            currentDayTurnIndex++
            getMoveAndSetPhase(
                move,
                PhaseState.VOTE,
                currentDayTurnIndex >= players.size,
            )
        } else {
            getMoveAndSetPhase(null, PhaseState.VOTE)
        }
    }

    private fun nextLastWillMove(): Moves? {
        val lastWillPlayer = domainRepository.pendingPlayers.firstOrNull()
        return if (lastWillPlayer != null) {
            domainRepository.removePendingPlayers(lastWillPlayer)
            getMoveAndSetPhase(
                Moves.LastWill(lastWillPlayer),
                PhaseState.CHECK_WIN,
                domainRepository.pendingPlayers.isEmpty(),
            )
        } else {
            getMoveAndSetPhase(null, PhaseState.CHECK_WIN)
        }
    }

    private fun nextCheckWinMove(): Moves? {
        if (domainRepository.pendingPlayers.isNotEmpty()) {
            return getMoveAndSetPhase(null, PhaseState.LAST_WILL)
        }
        return if (getWinCondition()) {
            getMoveAndSetPhase(Moves.EndGame, PhaseState.END)
        } else {
            val phase = if (wasNightPhase) PhaseState.DAY_TURN else PhaseState.NIGHT_MAFIA
            if (wasNightPhase) {
                wasNightPhase = false
                isNewDayTurn = true
            } else {
                wasNightPhase = true
            }
            getMoveAndSetPhase(null, phase)
        }
    }

    private fun nextNightMafiaMove(): Moves? {
        wasNightPhase = true
        return getMoveAndSetPhase(Moves.NightAction(Role.MAFIA), PhaseState.NIGHT_DON)
    }

    private fun nextNightRoleMove(
        role: Role,
        nextPhase: PhaseState,
    ): Moves? {
        val move =
            if (domainRepository.allPlayers.any { it.role == role }) {
                Moves.NightAction(role)
            } else {
                null
            }
        return getMoveAndSetPhase(move, nextPhase)
    }

    private fun getMoveAndSetPhase(
        move: Moves?,
        nextPhase: PhaseState,
        changePhase: Boolean = true,
    ): Moves? {
        if (changePhase) phaseState = nextPhase
        return move
    }

    private fun getWinCondition(): Boolean {
        val players = domainRepository.players.value
        val mafia = players.filter { it.role == Role.MAFIA || it.role == Role.DON }
        val citizens = players - mafia
        return mafia.isEmpty() || mafia.size >= citizens.size
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
