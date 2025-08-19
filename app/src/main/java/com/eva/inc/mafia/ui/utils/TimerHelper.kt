package com.eva.inc.mafia.ui.utils

import android.os.CountDownTimer

class TimerHelper(
    private val totalMillis: Long,
    private val intervalMillis: Long = 1000L,
    private val tick: (millisUntilFinished: Long) -> Unit,
    private val finish: () -> Unit,
) {
    private var countDownTimer: CountDownTimer? = null
    private var timeLeft: Long = totalMillis
    private var running: Boolean = false

    val isRunning: Boolean get() = running
    val timeLeftMillis: Long get() = timeLeft

    fun start() {
        if (running) return
        countDownTimer =
            object : CountDownTimer(timeLeft, intervalMillis) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft = millisUntilFinished
                    tick(millisUntilFinished)
                }

                override fun onFinish() {
                    running = false
                    timeLeft = 0L
                    finish()
                }
            }.start()
        running = true
    }

    fun pause() {
        countDownTimer?.cancel()
        running = false
    }

    fun reset() {
        countDownTimer?.cancel()
        timeLeft = totalMillis
        running = false
        tick(timeLeft)
    }

    fun cancel() {
        countDownTimer?.cancel()
        running = false
    }
}
