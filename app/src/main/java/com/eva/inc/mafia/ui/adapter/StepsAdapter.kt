package com.eva.inc.mafia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eva.inc.mafia.R
import com.eva.inc.mafia.databinding.ListItemNumberBinding
import com.eva.inc.mafia.databinding.ListItemStepBinding
import com.eva.inc.mafia.ui.adapter.StepsAdapter.MovesViewHolder
import com.eva.inc.mafia.ui.entity.Role
import com.eva.inc.mafia.ui.entity.Step

class StepsAdapter(
    private val items: List<Step>,
) : RecyclerView.Adapter<MovesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = MovesViewHolder(
        ListItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(
        holder: MovesViewHolder,
        position: Int,
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class MovesViewHolder(
        private val binding: ListItemStepBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Step) {
            binding.imgIcon.setImageResource(item.role.drawable)

            item.numbers.forEach {
                val numberBinding =
                    ListItemNumberBinding.inflate(
                        LayoutInflater.from(binding.root.context),
                        binding.root,
                        false,
                    )
                numberBinding.btnNumber.text = it?.number?.toString() ?: "x"

                val color =
                    when (it?.role) {
                        Role.DON, Role.MAFIA -> R.color.mafia
                        null -> null
                        else -> R.color.civilian
                    }
                if (color != null) {
                    numberBinding.btnNumber.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            color,
                        ),
                    )
                }
                binding.root.addView(numberBinding.root)
            }
        }
    }
}
