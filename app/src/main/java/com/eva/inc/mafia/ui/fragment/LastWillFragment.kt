package com.eva.inc.mafia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentLastWillBinding
import com.eva.inc.mafia.ui.entity.Move
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.getParcelableCompat
import com.eva.inc.mafia.ui.utils.lazyUi

class LastWillFragment : BaseFragment<FragmentLastWillBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLastWillBinding =
        FragmentLastWillBinding::inflate

    private val lastWill: Move.LastWill by lazyUi {
        arguments.getParcelableCompat(ARG_LAST_WILL)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val player = lastWill.player
        binding.textViewPlayerName.text = player.toString()
        binding.textViewPlayerRole.text = "Роль: ${getString(player.role.title)}"
    }

    companion object {
        private const val ARG_LAST_WILL = "arg_last_will"

        fun newInstance(lastWill: Move.LastWill): LastWillFragment =
            LastWillFragment().apply {
                arguments =
                    Bundle().apply {
                        putParcelable(ARG_LAST_WILL, lastWill)
                    }
            }
    }
}
