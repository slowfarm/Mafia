package com.eva.inc.mafia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.R
import com.eva.inc.mafia.databinding.ListItemPlayerBinding
import com.eva.inc.mafia.ui.entity.Player

class PlayersAdapter : RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder>() {
    private val items =
        mutableListOf<Player>().apply {
            for (i in 1..6) add(Player.create(i, "Player $i"))
        }

    val players: List<Player> get() = items.toList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = PlayerViewHolder(
        ListItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(
        holder: PlayerViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun addItem() {
        val position = items.size
        items.add(Player.create(position + 1))
        notifyItemInserted(position)
    }

    fun removeItem() {
        if (items.isNotEmpty()) {
            val position = items.lastIndex
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    inner class PlayerViewHolder(
        private val binding: ListItemPlayerBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Player) =
            with(binding) {
                val context = root.context

                btnNumber.text = item.number.toString()
                etName.hint = context.getString(R.string.input_name)
                if (etName.text.toString() != item.name) etName.setText(item.name)

                etName.doAfterTextChanged { text -> updatePlayer { it.copy(name = text.toString()) } }
            }

        private inline fun updatePlayer(
            notify: Boolean = false,
            crossinline updater: (Player) -> Player,
        ) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                items[position] = updater(items[position])
                if (notify) notifyItemChanged(position)
            }
        }
    }
}
