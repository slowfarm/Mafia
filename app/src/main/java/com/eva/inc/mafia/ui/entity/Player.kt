package com.eva.inc.mafia.ui.entity


data class Player(
    val number: Int,
    val name: String? = null,
    val role: Role? = null,
    val winningPoint: Float? = null,
    val additionalPoint: Float? = null,
) {
    companion object {
        fun create(number: Int) = Player(number)

        fun List<Player>.getUniqueRoles() = mapNotNull { it.role }.distinct()
    }
}
