package com.eva.inc.mafia.ui.adapter

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.R
import com.eva.inc.mafia.databinding.ListItemMoveBinding
import com.eva.inc.mafia.ui.adapter.MovesAdapter.MovesViewHolder
import com.eva.inc.mafia.ui.entity.Move
import com.eva.inc.mafia.ui.entity.Role

class MovesAdapter(roles: List<Role>, step: Int) : RecyclerView.Adapter<MovesViewHolder>() {
    private var items = roles.map { Move(it, MutableList(step) { null }) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovesViewHolder(
        ListItemMoveBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(holder: MovesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class MovesViewHolder(private val binding: ListItemMoveBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Move) {
            binding.imgIcon.setImageResource(item.role.drawable)

            item.numbers.forEachIndexed { index, item ->
                binding.root.addView(getNumberEditText(index))
            }
        }

        private fun getNumberEditText(index: Int) =
            EditText(binding.root.context, null, androidx.appcompat.R.attr.editTextStyle).apply {
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    marginStart = resources.getDimensionPixelSize(R.dimen.dp8)
                }

                hint = context.getString(R.string.x)
                inputType = InputType.TYPE_CLASS_NUMBER

                doAfterTextChanged { text ->
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        items[position].numbers[index] = text.toString().toIntOrNull()
                    }
                }
            }
    }
}