package com.bravedroid.connecta.ui

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt

object ViewExt {

    fun colorStateListOf(@ColorInt color: Int): ColorStateList {
        return ColorStateList.valueOf(color)
    }

    fun colorStateListOf(vararg mapping: Pair<IntArray, Int>): ColorStateList {
        val (states, colors) = mapping.unzip()
        return ColorStateList(states.toTypedArray(), colors.toIntArray())
    }

    val colorList = colorStateListOf(
        intArrayOf(-android.R.attr.state_enabled) to Color.BLACK,
        intArrayOf(android.R.attr.state_enabled) to Color.RED,
    )

}
