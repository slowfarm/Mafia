package com.eva.inc.mafia.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class VoteArrayAdapter<T>(
    context: Context,
    items: List<T>,
) : ArrayAdapter<T>(
        context,
        android.R.layout.simple_spinner_item,
        items,
    ) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View {
        val view = super.getView(position, convertView, parent)
        (view as? android.widget.TextView)?.text = getItem(position).toString()
        return view
    }

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
    ): View {
        val view = super.getDropDownView(position, convertView, parent)
        (view as? android.widget.TextView)?.text = getItem(position).toString()
        return view
    }
}
