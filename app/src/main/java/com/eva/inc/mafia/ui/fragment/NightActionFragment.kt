package com.eva.inc.mafia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentNightActionBinding
import com.eva.inc.mafia.ui.App
import com.eva.inc.mafia.ui.adapter.ExhibitedPlayersAdapter
import com.eva.inc.mafia.ui.entity.Move
import com.eva.inc.mafia.ui.entity.Role
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.collectWithLifecycle
import com.eva.inc.mafia.ui.utils.getParcelableCompat
import com.eva.inc.mafia.ui.utils.lazyUi

class NightActionFragment : BaseFragment<FragmentNightActionBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNightActionBinding =
        FragmentNightActionBinding::inflate

    private val domainRepository = App.get().domainRepository

    private val nightAction: Move.NightAction by lazy {
        arguments.getParcelableCompat(ARG_NIGHT_ACTION)
    }

    private val adapter by lazyUi { ExhibitedPlayersAdapter(true) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewResult.text = ""

        binding.recyclerViewPlayers.adapter = adapter

        when (nightAction.role) {
            Role.MAFIA -> setupForMafia()
            Role.DON -> setupForDon()
            Role.SHERIFF -> setupForSheriff()
            else -> setupForOthers()
        }

        collectWithLifecycle(domainRepository.players) { players ->
            val items =
                players.map {
                    ExhibitedPlayersAdapter.ExhibitedPlayer(
                        it,
                        it.toString(),
                        false,
                    )
                }
            adapter.setItems(items)
        }
    }

    private fun setupForMafia() =
        with(binding) {
            textViewPrompt.text = "Мафия, выберите одного игрока для устранения"

            btnNightAction.text = "Убрать из игры"

            btnNightAction.setOnClickListener {
                val item = adapter.getSelectedPlayers().firstOrNull()
                if (item != null) {
                    textViewResult.text = "$item убран мафией"
                    domainRepository.pendingPlayers.add(item)
                }
            }
        }

    private fun setupForDon() =
        with(binding) {
            textViewPrompt.text = "Дон, выберите игрока для проверки на Шерифа"
            btnNightAction.text = "Проверить"

            btnNightAction.setOnClickListener {
                val item = adapter.getSelectedPlayers().firstOrNull()
                if (item != null) {
                    val isSheriff = item.role == Role.SHERIFF
                    textViewResult.text = "Игрок $item — ${if (!isSheriff) "не" else ""} Шериф"
                }
            }
        }

    private fun setupForSheriff() =
        with(binding) {
            textViewPrompt.text = "Шериф, выберите игрока для проверки на Дона"
            btnNightAction.text = "Проверить"

            btnNightAction.setOnClickListener {
                val item = adapter.getSelectedPlayers().firstOrNull()
                if (item != null) {
                    val isDon = item.role == Role.DON
                    textViewResult.text = "Игрок $item — ${if (!isDon) "не" else ""} Дон"
                }
            }
        }

    private fun setupForOthers() =
        with(binding) {
            textViewPrompt.text = "Эта роль не участвует в ночных действиях"
            recyclerViewPlayers.visibility = View.GONE
            btnNightAction.visibility = View.GONE
        }

    companion object {
        private const val ARG_NIGHT_ACTION = "arg_night_action"

        fun newInstance(nightAction: Move.NightAction): NightActionFragment =
            NightActionFragment().apply {
                arguments =
                    Bundle().apply {
                        putParcelable(ARG_NIGHT_ACTION, nightAction)
                    }
            }
    }
}
