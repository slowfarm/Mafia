package com.eva.inc.mafia.ui.fragment.nightaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentNightActionBinding
import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.adapter.ExhibitedPlayersAdapter
import com.eva.inc.mafia.ui.entity.Moves
import com.eva.inc.mafia.ui.entity.Role
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.collectWithLifecycle
import com.eva.inc.mafia.ui.utils.getParcelableCompat
import com.eva.inc.mafia.ui.utils.lazyUi

class NightActionFragment : BaseFragment<FragmentNightActionBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNightActionBinding =
        FragmentNightActionBinding::inflate

    private val nightAction: Moves.NightAction by lazy {
        arguments.getParcelableCompat(ARG_NIGHT_ACTION)
    }

    private val adapter by lazyUi { ExhibitedPlayersAdapter() }

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

        collectWithLifecycle(DomainRepository.players) { adapter.setItems(it) }
    }

    private fun setupForMafia() =
        with(binding) {
            textViewPrompt.text = "Мафия, выберите одного игрока для устранения"

            btnNightAction.text = "Убрать из игры"

            btnNightAction.setOnClickListener {
                val selected = adapter.getSelectedPlayers()
                if (selected.size == 1) {
                    val target = selected[0]
                    textViewResult.text =
                        "Игрок №${target.number} ${target.name.orEmpty()} убран мафией"
                }
                DomainRepository.removePlayers(selected)
            }
        }

    private fun setupForDon() =
        with(binding) {
            textViewPrompt.text = "Дон, выберите игрока для проверки на Шерифа"
            btnNightAction.text = "Проверить"

            btnNightAction.setOnClickListener {
                val selected = adapter.getSelectedPlayers()
                if (selected.size == 1) {
                    val target = selected[0]
                    val isSheriff = target.role == Role.SHERIFF
                    textViewResult.text =
                        if (isSheriff) {
                            "Игрок №${target.number} — Шериф"
                        } else {
                            "Игрок №${target.number} — не Шериф"
                        }
                }
            }
        }

    private fun setupForSheriff() =
        with(binding) {
            textViewPrompt.text = "Шериф, выберите игрока для проверки на Дона"
            btnNightAction.text = "Проверить"

            btnNightAction.setOnClickListener {
                val selected = adapter.getSelectedPlayers()
                if (selected.size == 1) {
                    val target = selected[0]
                    val isDon = target.role == Role.DON
                    textViewResult.text =
                        if (isDon) {
                            "Игрок №${target.number} — Дон"
                        } else {
                            "Игрок №${target.number} — не Дон"
                        }
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

        fun newInstance(nightAction: Moves.NightAction): NightActionFragment =
            NightActionFragment().apply {
                arguments =
                    Bundle().apply {
                        putParcelable(ARG_NIGHT_ACTION, nightAction)
                    }
            }
    }
}
