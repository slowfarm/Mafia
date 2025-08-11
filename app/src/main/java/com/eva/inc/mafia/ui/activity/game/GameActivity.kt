package com.eva.inc.mafia.ui.activity.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.databinding.ActivityGameBinding
import com.eva.inc.mafia.ui.activity.base.BaseActivity
import com.eva.inc.mafia.ui.adapter.PlayersAdapter
import com.eva.inc.mafia.ui.utils.showMovesDialog

class GameActivity : BaseActivity<ActivityGameBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityGameBinding =
        ActivityGameBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = PlayersAdapter()
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvPlayers.smoothScrollToPosition(adapter.itemCount - 1)
            }
        })
        binding.rvPlayers.adapter = adapter


        binding.fabAdd.setOnClickListener { adapter.addItem() }
        binding.btnMoves.setOnClickListener { showMovesDialog(this, adapter.getRoles(), 6) }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, GameActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}