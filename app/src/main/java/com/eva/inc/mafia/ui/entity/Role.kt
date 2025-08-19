package com.eva.inc.mafia.ui.entity

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eva.inc.mafia.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Role(
    @StringRes val title: Int,
    @DrawableRes val drawable: Int,
) : Parcelable {
    CIVILIAN(R.string.civilian, R.drawable.ic_civilian),
    MAFIA(R.string.mafia, R.drawable.ic_mafia),
    DON(R.string.don, R.drawable.ic_don),
    SHERIFF(R.string.sheriff, R.drawable.ic_sheriff),
    DOCTOR(R.string.doctor, R.drawable.ic_doctor),
    MANIAC(R.string.maniac, R.drawable.ic_maniac),
    WEREWOLF(R.string.werewolf, R.drawable.ic_warewolf),
    JOURNALIST(R.string.journalist, R.drawable.ic_journalist),
    YAKUZA(R.string.yakuza, R.drawable.ic_yakuza),
}
