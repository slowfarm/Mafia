package com.eva.inc.mafia.ui.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Player(
    val number: Int,
    val name: String? = null,
    val failsCount: Int = 0,
    val role: Role = Role.CIVILIAN,
) : Parcelable {
    override fun toString(): String = "Игрок №$number $name"

    companion object {
        fun create(
            number: Int,
            name: String? = null,
        ) = Player(number, name)

        fun generate(playerCount: Int): List<Role> {
            val roles = mutableListOf<Role>()

            when {
                playerCount < 6 -> repeat(playerCount) { roles.add(Role.CIVILIAN) }
                else -> {
                    roles.add(Role.MAFIA)
                    roles.add(Role.DON)
                    roles.add(Role.SHERIFF)
                    repeat(playerCount - roles.size) { roles.add(Role.CIVILIAN) }
                    if (playerCount >= 9) roles.add(Role.MAFIA)
                    if (playerCount >= 11) roles.add(Role.MANIAC)
                    if (playerCount >= 12) roles.add(Role.DOCTOR)
                    if (playerCount >= 13) roles.add(Role.WEREWOLF)
                    if (playerCount >= 14) roles.add(Role.JOURNALIST)
                    if (playerCount >= 15) roles.add(Role.YAKUZA)
                }
            }

            return roles.shuffled(Random(System.currentTimeMillis()))
        }
    }
}
