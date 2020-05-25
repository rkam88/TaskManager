package com.langfordapps.taskmanager.commons.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}