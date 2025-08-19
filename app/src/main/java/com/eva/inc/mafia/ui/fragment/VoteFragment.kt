package com.eva.inc.mafia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eva.inc.mafia.databinding.FragmentVoteBinding
import com.eva.inc.mafia.domain.repository.DomainRepository
import com.eva.inc.mafia.ui.adapter.ExhibitedPlayersAdapter
import com.eva.inc.mafia.ui.fragment.base.BaseFragment
import com.eva.inc.mafia.ui.utils.collectWithLifecycle
import com.eva.inc.mafia.ui.utils.lazyUi

class VoteFragment : BaseFragment<FragmentVoteBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentVoteBinding =
        FragmentVoteBinding::inflate

    private val adapter by lazyUi { ExhibitedPlayersAdapter() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewExhibitedPlayers.adapter = adapter

        binding.btnRemoveSelected.setOnClickListener {
            DomainRepository.setPendingEliminatedPlayers(adapter.getSelectedPlayers())
        }

        collectWithLifecycle(DomainRepository.exhibitedPlayers) { players ->
            val items =
                players.map {
                    ExhibitedPlayersAdapter.ExhibitedPlayer(
                        it,
                        it.toString(),
                        false,
                    )
                }
            adapter.setItems(items)
        }
    }

    companion object {
        fun newInstance(): VoteFragment = VoteFragment()
    }
}
