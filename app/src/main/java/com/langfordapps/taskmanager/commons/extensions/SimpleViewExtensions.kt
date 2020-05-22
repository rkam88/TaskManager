package com.langfordapps.taskmanager.commons.extensions

import android.graphics.Paint
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView

fun Spinner.doOnItemSelected(onItemSelected: (Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(position)
        }

    }
}

fun TextView.setStrikeThrough(enabled: Boolean) {
    paintFlags = if (enabled) {
        paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}