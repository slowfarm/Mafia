package com.eva.inc.mafia.domain

import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.entity.GameSnapshot
import com.eva.inc.mafia.ui.entity.Move
import com.eva.inc.mafia.ui.entity.Role

class GameFlowManager(
    private val domainRepository: DomainRepository,
) {
    private var phaseState = PhaseState.ROLE_ASSIGNMENT
    private var wasNightPhase = false
    private var currentDayTurnIndex = 0
    private var currentRoleAssignmentIndex = 0
    private var isNewDayTurn = true

    fun startGame(): Move? {
        val snapshot = domainRepository.loadSnapshotFromPrefs()

        if (snapshot != null) {
            domainRepository.restore(snapshot)
            phaseState = PhaseState.valueOf(snapshot.phaseState)
            wasNightPhase = snapshot.wasNightPhase
            currentDayTurnIndex = snapshot.currentDayTurnIndex
            currentRoleAssignmentIndex = snapshot.currentRoleAssignmentIndex
            isNewDayTurn = snapshot.isNewDayTurn
        } else {
            resetState()
        }
        return nextSingleMove()
    }

    fun nextSingleMove(): Move? {
        if (phaseState == PhaseState.END) return null

        val move =
            when (phaseState) {
                PhaseState.ROLE_ASSIGNMENT -> nextRoleAssignmentMove()
                PhaseState.MAFIA_MEETUP -> nextMafiaMeetup()
                PhaseState.DAY_TURN -> nextDayTurnMove()
                PhaseState.VOTE -> getMoveAndSetPhase(Move.Vote, PhaseState.CHECK_WIN)
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

    private fun nextMafiaMeetup(): Move? {
        currentDayTurnIndex = 0
        wasNightPhase = false
        isNewDayTurn = true
        return getMoveAndSetPhase(Move.MafiaMeetUp, PhaseState.DAY_TURN)
    }

    private fun nextRoleAssignmentMove(): Move? {
        val players = domainRepository.players.value
        return if (currentRoleAssignmentIndex < players.size) {
            val move = Move.RoleAssignment(players[currentRoleAssignmentIndex])
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

    private fun nextDayTurnMove(): Move? {
        if (isNewDayTurn) {
            currentDayTurnIndex = 0
            isNewDayTurn = false
        }
        val players = domainRepository.players.value
        return if (currentDayTurnIndex < players.size) {
            val move = Move.DayTurn(players[currentDayTurnIndex])
            currentDayTurnIndex++
            getMoveAndSetPhase(move, PhaseState.VOTE, currentDayTurnIndex >= players.size)
        } else {
            getMoveAndSetPhase(null, PhaseState.VOTE)
        }
    }

    private fun nextLastWillMove(): Move? {
        val lastWillPlayer = domainRepository.pendingPlayers.firstOrNull()
        return if (lastWillPlayer != null) {
            domainRepository.removePendingPlayers(lastWillPlayer)
            getMoveAndSetPhase(
                Move.LastWill(lastWillPlayer),
                PhaseState.CHECK_WIN,
                domainRepository.pendingPlayers.isEmpty(),
            )
        } else {
            getMoveAndSetPhase(null, PhaseState.CHECK_WIN)
        }
    }

    private fun nextCheckWinMove(): Move? {
        if (domainRepository.pendingPlayers.isNotEmpty()) {
            return getMoveAndSetPhase(null, PhaseState.LAST_WILL)
        }
        return if (getWinCondition()) {
            getMoveAndSetPhase(Move.EndGame, PhaseState.END)
        } else {
            val nextPhase =
                if (wasNightPhase) {
                    wasNightPhase = false
                    isNewDayTurn = true
                    PhaseState.DAY_TURN
                } else {
                    wasNightPhase = true
                    PhaseState.NIGHT_MAFIA
                }
            getMoveAndSetPhase(null, nextPhase)
        }
    }

    private fun nextNightMafiaMove(): Move? {
        wasNightPhase = true
        return getMoveAndSetPhase(Move.NightAction(Role.MAFIA), PhaseState.NIGHT_DON)
    }

    private fun nextNightRoleMove(
        role: Role,
        nextPhase: PhaseState,
    ): Move? {
        val move =
            if (domainRepository.allPlayers.any { it.role == role }) {
                Move.NightAction(role)
            } else {
                null
            }
        return getMoveAndSetPhase(move, nextPhase)
    }

    private fun getMoveAndSetPhase(
        move: Move?,
        nextPhase: PhaseState,
        changePhase: Boolean = true,
    ): Move? {
        if (changePhase) phaseState = nextPhase
        if (move != null) domainRepository.addSnapshot(createSnapshot())
        return move
    }

    private fun getWinCondition(): Boolean {
        val players = domainRepository.players.value
        val mafia = players.count { it.role == Role.MAFIA || it.role == Role.DON }
        val citizens = players.size - mafia
        return mafia == 0 || mafia >= citizens
    }

    private fun createSnapshot() =
        GameSnapshot(
            players = domainRepository.players.value,
            exhibitedPlayers = domainRepository.exhibitedPlayers.value.toList(),
            pendingPlayers = domainRepository.pendingPlayers.toList(),
            allPlayers = domainRepository.allPlayers,
            steps = domainRepository.steps,
            phaseState = phaseState.name,
            wasNightPhase = wasNightPhase,
            currentDayTurnIndex = currentDayTurnIndex,
            currentRoleAssignmentIndex = currentRoleAssignmentIndex,
            isNewDayTurn = isNewDayTurn,
        )

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
