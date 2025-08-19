package com.eva.inc.mafia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.databinding.ListItemExhibitedPlayerBinding
import com.eva.inc.mafia.ui.adapter.ExhibitedPlayersAdapter.ExhibitedPlayersViewHolder
import com.eva.inc.mafia.ui.entity.Player

class ExhibitedPlayersAdapter : RecyclerView.Adapter<ExhibitedPlayersViewHolder>() {
    private val selectedItems = mutableSetOf<Int>()

    private var items = listOf<Player>()

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

    fun setItems(items: List<Player>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getSelectedPlayers(): List<Player> = selectedItems.map { items[it] }

    inner class ExhibitedPlayersViewHolder(
        private val binding: ListItemExhibitedPlayerBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(player: Player) =
            with(binding) {
                val selected = selectedItems.contains(bindingAdapterPosition)

                checkboxSelected.setOnCheckedChangeListener(null)
                textViewPlayer.text = "Игрок №${player.number} ${player.name.orEmpty()}"
                checkboxSelected.isChecked = selected
                checkboxSelected.setOnCheckedChangeListener { _, isChecked -> setSelected(isChecked) }
                root.setOnClickListener { setSelected(!selected) }
            }

        private fun setSelected(selected: Boolean) {
            if (selected) {
                selectedItems.add(bindingAdapterPosition)
            } else {
                selectedItems.remove(bindingAdapterPosition)
            }
            notifyItemChanged(bindingAdapterPosition)
        }
    }
}
