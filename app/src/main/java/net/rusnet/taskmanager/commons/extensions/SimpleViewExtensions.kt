package net.rusnet.taskmanager.commons.extensions

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

fun Spinner.doOnItemSelected(onItemSelected: (Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            onItemSelected(position)
        }

    }
}