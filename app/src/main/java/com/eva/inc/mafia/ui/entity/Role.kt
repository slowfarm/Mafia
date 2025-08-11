package com.eva.inc.mafia.ui.entity

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.eva.inc.mafia.R

enum class Role(
    @IdRes val itemId: Int,
    @StringRes val title: Int,
    @DrawableRes val drawable: Int,
) {
    CIVILIAN(R.id.item_civilian, R.string.civilian, R.drawable.ic_civilian),
    MAFIA(R.id.item_mafia, R.string.mafia, R.drawable.ic_mafia),
    DON(R.id.item_don, R.string.don, R.drawable.ic_don),
    SHERIFF(R.id.item_sheriff, R.string.sheriff, R.drawable.ic_sheriff),
    DOCTOR(R.id.item_doctor, R.string.doctor, R.drawable.ic_doctor),
    MANIAC(R.id.item_maniac, R.string.maniac, R.drawable.ic_maniac),
    WEREWOLF(R.id.item_werewolf, R.string.werewolf, R.drawable.ic_warewolf),
    JOURNALIST(R.id.item_journalist, R.string.journalist, R.drawable.ic_journalist),
    YAKUZA(R.id.item_yakuza, R.string.yakuza, R.drawable.ic_yakuza);

    companion object {
        fun fromItemId(@IdRes itemId: Int) = Role.entries.find { it.itemId == itemId }
    }
}
