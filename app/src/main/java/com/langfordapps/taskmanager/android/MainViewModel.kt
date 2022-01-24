package com.langfordapps.taskmanager.android

import androidx.lifecycle.ViewModel
import com.langfordapps.taskmanager.app.AppBloc

class MainViewModel(
    private val appBloc: AppBloc
): ViewModel(), AppBloc by appBloc {

    override fun onCleared() {
        appBloc.onClear()
        super.onCleared()
    }

}