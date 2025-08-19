package com.eva.inc.mafia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.databinding.ListItemPlayerRoleBinding
import com.eva.inc.mafia.ui.adapter.PlayersRoleAdapter.PlayerViewHolder
import com.eva.inc.mafia.ui.entity.Player

class PlayersRoleAdapter : RecyclerView.Adapter<PlayerViewHolder>() {
    private var items: List<Player> = emptyList()

    fun setItems(players: List<Player>) {
        items = players
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = PlayerViewHolder(
        ListItemPlayerRoleBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(
        holder: PlayerViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    class PlayerViewHolder(
        private val binding: ListItemPlayerRoleBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) =
            with(binding) {
                textViewPlayerName.text = "Игрок №${player.number} ${player.name.orEmpty()}"
                textViewPlayerRole.setText(player.role.title)
            }
    }
}
