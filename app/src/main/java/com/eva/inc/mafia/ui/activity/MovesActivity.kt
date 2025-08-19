package com.eva.inc.mafia.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.eva.inc.mafia.databinding.ActivityMovesBinding
import com.eva.inc.mafia.domain.GameFlowManager
import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.activity.base.BaseActivity
import com.eva.inc.mafia.ui.adapter.MovesPagerAdapter
import com.eva.inc.mafia.ui.entity.Moves
import com.eva.inc.mafia.ui.entity.Player
import com.eva.inc.mafia.ui.utils.collectWithLifecycle
import com.eva.inc.mafia.ui.utils.lazyUi

class MovesActivity : BaseActivity<ActivityMovesBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMovesBinding =
        ActivityMovesBinding::inflate

    private val adapter by lazyUi { MovesPagerAdapter(this) }
    private val manager = GameFlowManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewPager.adapter = adapter

        manager.startGame()?.let {
            adapter.addItem(it)
            binding.viewPager.currentItem = 0
        }

        binding.btnNextMove.setOnClickListener {
            manager.nextSingleMove()?.let {
                if (it == Moves.EndGame) {
                    binding.btnNextMove.text = "Новая игра"
                    binding.btnNextMove.setOnClickListener { GameActivity.start(this) }
                }
                adapter.addItem(it)
                binding.viewPager.currentItem = adapter.itemCount - 1
            }
        }

        collectWithLifecycle(DomainRepository.exhibitedPlayers) { players ->
            updateToolbarTitle(players)
        }
    }

    private fun updateToolbarTitle(players: Set<Player>) {
        val title =
            if (players.isNotEmpty()) {
                "Выставлены: " + players.map { it.number }.joinToString(", ")
            } else {
                "Никто не выставлен"
            }
        binding.toolbar.title = title
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MovesActivity::class.java))
        }
    }
}
