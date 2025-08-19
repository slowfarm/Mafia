package com.eva.inc.mafia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.databinding.ListItemExhibitedPlayerBinding
import com.eva.inc.mafia.ui.adapter.ExhibitedPlayersAdapter.ExhibitedPlayersViewHolder
import com.eva.inc.mafia.ui.entity.Player

class ExhibitedPlayersAdapter(
    private val singleSelection: Boolean = false,
) : RecyclerView.Adapter<ExhibitedPlayersViewHolder>() {
    private var items = mutableListOf<ExhibitedPlayer>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = ExhibitedPlayersViewHolder(
        ListItemExhibitedPlayerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        ),
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(
        holder: ExhibitedPlayersViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    fun setItems(items: List<ExhibitedPlayer>) {
        this.items = items.toMutableList()
        notifyDataSetChanged()
    }

    fun getSelectedPlayers() = items.filter { it.selected }.map { it.player }

    inner class ExhibitedPlayersViewHolder(
        private val binding: ListItemExhibitedPlayerBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: ExhibitedPlayer) =
            with(binding) {
                checkboxSelected.setOnCheckedChangeListener(null)
                textViewPlayer.text = player.name
                checkboxSelected.isChecked = player.selected
                checkboxSelected.setOnCheckedChangeListener { _, checked ->
                    if (singleSelection) {
                        val index = items.indexOfFirst { it.selected }
                        if (index != -1) {
                            items[index] = items[index].copy(selected = false)
                            notifyItemChanged(index)
                        }
                    }
                    val position = bindingAdapterPosition
                    items[position] = items[position].copy(selected = checked)
                    notifyItemChanged(position)
                }
            }
    }

    data class ExhibitedPlayer(
        val player: Player,
        val name: String,
        val selected: Boolean,
    )
}
