package com.eva.inc.mafia.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.eva.inc.mafia.databinding.ViewCountdownTimerBinding
import com.eva.inc.mafia.ui.utils.TimerHelper

class CountdownTimerView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : LinearLayout(context, attrs, defStyleAttr) {
        private val binding =
            ViewCountdownTimerBinding.inflate(LayoutInflater.from(context), this, false)

        private val timerHelper =
            TimerHelper(
                totalMillis = (60 * 1000).toLong(),
                tick = { millisLeft -> updateCountdownText(millisLeft) },
                finish = { updateButtons() },
            )

        init {
            addView(binding.root)
            setupListeners()
            updateCountdownText(timerHelper.timeLeftMillis)
            updateButtons()
        }

        private fun setupListeners() {
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

        fun cancel() {
            timerHelper.cancel()
        }
    }
