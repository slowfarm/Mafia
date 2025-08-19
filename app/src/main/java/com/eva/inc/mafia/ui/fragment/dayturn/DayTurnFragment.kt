package com.eva.inc.mafia.ui.fragment.dayturn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.eva.inc.mafia.databinding.FragmentDayTurnBinding
import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.adapter.VoteArrayAdapter
import com.eva.inc.mafia.ui.entity.Moves
import com.eva.inc.mafia.ui.entity.Player
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.TimerHelper
import com.eva.inc.mafia.ui.utils.collectWithLifecycle
import com.eva.inc.mafia.ui.utils.getParcelableCompat

class DayTurnFragment : BaseFragment<FragmentDayTurnBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDayTurnBinding =
        FragmentDayTurnBinding::inflate

    private val dayTurn: Moves.DayTurn by lazy { arguments.getParcelableCompat(ARG_DAY_TURN) }

    private val timerHelper by lazy {
        TimerHelper(
            totalMillis = (60 * 1000).toLong(),
            tick = { millisLeft -> updateCountdownText(millisLeft) },
            finish = { updateButtons() },
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewSpeaker.text =
            "Говорит игрок №${dayTurn.player.number}, ${dayTurn.player.name}"
        binding.textViewFails.text = "Фолы: ${dayTurn.player.failsCount}"

        updateCountdownText(timerHelper.timeLeftMillis)
        updateButtons()

        binding.btnPlay.setOnClickListener {
            timerHelper.start()
            updateButtons()
        }
        binding.btnPause.setOnClickListener {
            timerHelper.pause()
            updateButtons()
        }
        binding.btnRestart.setOnClickListener {
            timerHelper.reset()
            updateButtons()
        }

        binding.btnVoteConfirm.setOnClickListener {
            val player = (binding.spinnerVote.selectedItem as? Player)
            player?.let { DomainRepository.addExhibitedPlayer(it) }
        }

        collectWithLifecycle(DomainRepository.players) {
            val adapter = VoteArrayAdapter(requireContext(), it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerVote.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerHelper.cancel()
    }

    private fun updateCountdownText(millisUntilFinished: Long) {
        val secondsLeft = (millisUntilFinished / 1000).toInt()
        val minutes = secondsLeft / 60
        val seconds = secondsLeft % 60
        binding.textViewTimer.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateButtons() {
        binding.btnPlay.isVisible = !timerHelper.isRunning && timerHelper.timeLeftMillis > 0
        binding.btnPause.isVisible = timerHelper.isRunning
        binding.btnRestart.isVisible =
            !timerHelper.isRunning &&
            timerHelper.timeLeftMillis < (60 * 1000).toLong()
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
