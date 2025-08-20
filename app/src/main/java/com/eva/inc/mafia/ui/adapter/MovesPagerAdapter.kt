package com.eva.inc.mafia.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eva.inc.mafia.ui.entity.Move
import com.eva.inc.mafia.ui.entity.Move.DayTurn
import com.eva.inc.mafia.ui.entity.Move.EndGame
import com.eva.inc.mafia.ui.entity.Move.LastWill
import com.eva.inc.mafia.ui.entity.Move.MafiaMeetUp
import com.eva.inc.mafia.ui.entity.Move.NightAction
import com.eva.inc.mafia.ui.entity.Move.RoleAssignment
import com.eva.inc.mafia.ui.entity.Move.Vote
import com.eva.inc.mafia.ui.fragment.DayTurnFragment
import com.eva.inc.mafia.ui.fragment.EndGameFragment
import com.eva.inc.mafia.ui.fragment.LastWillFragment
import com.eva.inc.mafia.ui.fragment.MafiaMeetUpFragment
import com.eva.inc.mafia.ui.fragment.NightActionFragment
import com.eva.inc.mafia.ui.fragment.RoleAssignmentFragment
import com.eva.inc.mafia.ui.fragment.VoteFragment

class MovesPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {
    private var items = mutableListOf<Move>()

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment {
        val move = items[position]
        return when (move) {
            is RoleAssignment -> RoleAssignmentFragment.newInstance(move)
            is MafiaMeetUp -> MafiaMeetUpFragment.newInstance()
            is DayTurn -> DayTurnFragment.newInstance(move)
            is Vote -> VoteFragment.newInstance()
            is NightAction -> NightActionFragment.newInstance(move)
            is LastWill -> LastWillFragment.newInstance(move)
            is EndGame -> EndGameFragment.newInstance()
        }
    }

    fun setItems(items: List<Move>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(move: Move) {
        items.add(move)
        notifyItemInserted(items.size - 1)
    }
}
