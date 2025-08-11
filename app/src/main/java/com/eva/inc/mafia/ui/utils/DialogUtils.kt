package com.eva.inc.mafia.ui.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import com.eva.inc.mafia.databinding.DialogMovesBinding
import com.eva.inc.mafia.ui.adapter.MovesAdapter
import com.eva.inc.mafia.ui.entity.Role

fun showMovesDialog(context: Context, roles: List<Role>, step: Int) {
    val binding = DialogMovesBinding.inflate(LayoutInflater.from(context))

    binding.rvMoves.adapter = MovesAdapter(roles, step)

    val dialog = AlertDialog.Builder(context)
        .setView(binding.root)
        .create()

    binding.btnClose.setOnClickListener { dialog.dismiss() }

    dialog.show()

    dialog.window?.apply {
        setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        setLayout(WRAP_CONTENT, WRAP_CONTENT)

        val minHeight = Resources.getSystem().displayMetrics.heightPixels
        val minWidth = Resources.getSystem().displayMetrics.widthPixels

        decorView.minimumHeight = minHeight
        decorView.minimumWidth = minWidth

        val params = attributes
        if (params.height < minHeight) {
            params.height = minHeight
            attributes = params
        }
    }
}
