package com.eva.inc.mafia.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.eva.inc.mafia.R
import com.google.android.material.checkbox.MaterialCheckBox

class FoulsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val checkBoxes: List<MaterialCheckBox>

    private var clickCount = 0

    init {
        orientation = HORIZONTAL
        background = ContextCompat.getDrawable(context, R.drawable.background_fouls_view)
        isClickable = true
        isFocusable = true

        checkBoxes = List(FOULS_COUNT) {
            MaterialCheckBox(context).apply {
                isClickable = false
                isFocusable = false
                addView(this)
            }
        }

        minimumHeight = resources.getDimensionPixelSize(R.dimen.material_button_min_height)

        val horizontalPadding =
            resources.getDimensionPixelSize(R.dimen.material_button_padding_horizontal)
        val verticalPadding =
            resources.getDimensionPixelSize(R.dimen.material_button_padding_vertical)
        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

        setOnClickListener {
            clickCount = (clickCount + 1) % 4
            checkBoxes.forEachIndexed { index, cb ->
                cb.isChecked = index < clickCount
            }
        }
    }

    companion object {
        private const val FOULS_COUNT = 3
    }
}
