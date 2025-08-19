package com.eva.inc.mafia.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eva.inc.mafia.ui.entity.Moves
import com.eva.inc.mafia.ui.entity.Moves.DayTurn
import com.eva.inc.mafia.ui.entity.Moves.EndGame
import com.eva.inc.mafia.ui.entity.Moves.LastWill
import com.eva.inc.mafia.ui.entity.Moves.MafiaMeetUp
import com.eva.inc.mafia.ui.entity.Moves.NightAction
import com.eva.inc.mafia.ui.entity.Moves.RoleAssignment
import com.eva.inc.mafia.ui.entity.Moves.Vote
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
    private var items = mutableListOf<Moves>()

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

    fun setItems(items: List<Moves>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItem(move: Moves) {
        items.add(move)
        notifyItemInserted(items.size - 1)
    }
}
