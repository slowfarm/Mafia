package com.eva.inc.mafia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.databinding.ListItemPlayerStatisticBinding
import com.eva.inc.mafia.ui.adapter.PlayersResultAdapter.PlayerViewHolder

class PlayersResultAdapter : RecyclerView.Adapter<PlayerViewHolder>() {
    private var items: List<PlayerResult> = emptyList()

    fun setItems(players: List<PlayerResult>) {
        items = players
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = PlayerViewHolder(
        ListItemPlayerStatisticBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(
        holder: PlayerViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    class PlayerViewHolder(
        private val binding: ListItemPlayerStatisticBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rolePlayer: PlayerResult) =
            with(binding) {
                imgIcon.setImageResource(rolePlayer.icon)
                textViewPlayerName.text = rolePlayer.name
                textViewPlayerRole.setText(rolePlayer.role)
                imgEliminated.isVisible = rolePlayer.eliminated
            }
    }

    data class PlayerResult(
        @DrawableRes val icon: Int,
        val name: String,
        @StringRes val role: Int,
        val eliminated: Boolean,
    )
}
