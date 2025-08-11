package com.eva.inc.mafia.ui.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.R
import com.eva.inc.mafia.databinding.ListItemPlayerBinding
import com.eva.inc.mafia.ui.adapter.PlayersAdapter.PlayerViewHolder
import com.eva.inc.mafia.ui.entity.Player
import com.eva.inc.mafia.ui.entity.Player.Companion.getUniqueRoles
import com.eva.inc.mafia.ui.entity.Role

class PlayersAdapter : RecyclerView.Adapter<PlayerViewHolder>() {
    private val items = mutableListOf<Player>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlayerViewHolder(
        ListItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem() {
        val position = items.size + 1
        items.add(Player.create(position))
        notifyItemInserted(position)
    }

    fun getRoles() = items.getUniqueRoles()

    inner class PlayerViewHolder(private val binding: ListItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Player) {
            val context = binding.root.context

            binding.btnNumber.text = item.number.toString()

            binding.etName.hint = context.getString(R.string.input_name)
            item.name?.let { binding.etName.setText(it) }
            binding.etName.doAfterTextChanged { text ->
                updatePlayer { it.copy(name = text.toString()) }
            }

            item.role?.let { binding.btnRole.setText(it.title) }
            binding.btnRole.setOnClickListener { view ->
                PopupMenu(context, view).apply {
                    Role.entries.forEach { menu.add(0, it.itemId, Menu.NONE, it.title) }

                    setOnMenuItemClickListener { menuItem ->
                        updatePlayer(true) { it.copy(role = Role.fromItemId(menuItem.itemId)) }
                        true
                    }
                }.show()
            }

            binding.etWinningPoint.hint = context.getString(R.string.point_hint)
            item.winningPoint?.let { binding.etWinningPoint.setText(it.toString()) }
            binding.etWinningPoint.doAfterTextChanged { text ->
                updatePlayer { it.copy(winningPoint = text.toString().toFloatOrNull() ?: 0f) }
            }

            binding.etAdditionalPoint.hint = context.getString(R.string.point_hint)
            item.additionalPoint?.let { binding.etAdditionalPoint.setText(it.toString()) }
            binding.etAdditionalPoint.doAfterTextChanged { text ->
                updatePlayer { it.copy(additionalPoint = text.toString().toFloatOrNull() ?: 0f) }
            }
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