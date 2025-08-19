package com.eva.inc.mafia.ui.activity.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.R
import com.eva.inc.mafia.databinding.ActivityGameBinding
import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.activity.base.BaseActivity
import com.eva.inc.mafia.ui.activity.moves.MovesActivity
import com.eva.inc.mafia.ui.adapter.PlayersAdapter

class GameActivity : BaseActivity<ActivityGameBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityGameBinding =
        ActivityGameBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        val adapter = PlayersAdapter()
        adapter.registerAdapterDataObserver(
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(
                    positionStart: Int,
                    itemCount: Int,
                ) {
                    binding.rvPlayers.smoothScrollToPosition(adapter.itemCount - 1)
                }
            },
        )
        binding.rvPlayers.adapter = adapter

        addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(
                    menu: Menu,
                    menuInflater: MenuInflater,
                ) {
                    menuInflater.inflate(R.menu.menu_game, menu)
                }

                override fun onMenuItemSelected(item: MenuItem): Boolean =
                    when (item.itemId) {
                        R.id.action_add -> {
                            adapter.addItem()
                            true
                        }
                        R.id.action_delete -> {
                            adapter.removeItem()
                            true
                        }
                        R.id.action_play -> {
                            DomainRepository.setPlayers(adapter.players)
                            MovesActivity.start(this@GameActivity)
                            true
                        }
                        else -> false
                    }
            },
        )
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, GameActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}
