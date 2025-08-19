package com.eva.inc.mafia.ui.fragment.mafiameetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.eva.inc.mafia.databinding.FragmentMafiaMeetUpBinding
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.TimerHelper

class MafiaMeetUpFragment : BaseFragment<FragmentMafiaMeetUpBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMafiaMeetUpBinding =
        FragmentMafiaMeetUpBinding::inflate

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
        fun newInstance(): MafiaMeetUpFragment = MafiaMeetUpFragment()
    }
}
