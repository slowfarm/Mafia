package com.eva.inc.mafia.ui.fragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentDayTurnBinding
import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.adapter.VoteArrayAdapter
import com.eva.inc.mafia.ui.entity.Moves
import com.eva.inc.mafia.ui.entity.Player
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.collectWithLifecycle
import com.eva.inc.mafia.ui.utils.getParcelableCompat

class DayTurnFragment : BaseFragment<FragmentDayTurnBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDayTurnBinding =
        FragmentDayTurnBinding::inflate

    private val dayTurn: Moves.DayTurn by lazy { arguments.getParcelableCompat(ARG_DAY_TURN) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewSpeaker.text =
            "Ход игрока №${dayTurn.player.number}, ${dayTurn.player.name}"
        binding.textViewFails.text = "Фолы: ${dayTurn.player.failsCount}"

        binding.btnVoteConfirm.setOnClickListener {
            val player = (binding.spinnerVote.selectedItem as? Player)
            player?.let { DomainRepository.addExhibitedPlayer(it) }
        }

        collectWithLifecycle(DomainRepository.players) {
            val adapter = VoteArrayAdapter(requireContext(), it)
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            binding.spinnerVote.adapter = adapter
        }
    }

    override fun onDestroyView() {
        binding.countdownTimer.cancel()
        super.onDestroyView()
    }

    companion object {
        private const val ARG_DAY_TURN = "arg_day_turn"

        fun newInstance(dayTurn: Moves.DayTurn): DayTurnFragment =
            DayTurnFragment().apply {
                arguments =
                    Bundle().apply {
                        putParcelable(ARG_DAY_TURN, dayTurn)
                    }
            }
    }
}
