package com.eva.inc.mafia.ui.fragment.endgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentEndGameBinding
import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.adapter.PlayersRoleAdapter
import com.eva.inc.mafia.ui.entity.Role
import com.eva.inc.mafia.ui.fragment.base.BaseFragment

class EndGameFragment : BaseFragment<FragmentEndGameBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentEndGameBinding =
        FragmentEndGameBinding::inflate

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val players = DomainRepository.players.value

        val mafiaCount = players.count { it.role == Role.MAFIA }
        val donCount = players.count { it.role == Role.DON }
        val mafiaTeamCount = mafiaCount + donCount

        val winMessage = if (mafiaTeamCount == 0) "Победа мирных" else "Победа мафии"

        binding.textViewEndGameMessage.text = "$winMessage. Итоги игроков:"

        val adapter = PlayersRoleAdapter()
        binding.recyclerViewPlayers.adapter = adapter
        adapter.setItems(DomainRepository.allPlayers)
    }

    companion object {
        fun newInstance(): EndGameFragment = EndGameFragment()
    }
}
