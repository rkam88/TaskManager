package com.langfordapps.taskmanager.app

import com.langfordapps.taskmanager.common.domain.Task
import com.langfordapps.taskmanager.core.bloc.BaseBlocParent
import com.langfordapps.taskmanager.tasks_display.TasksDisplayParent
import kotlinx.coroutines.flow.MutableStateFlow

class AppBlocImpl(
    private val factory: AppScreenFactory
) : AppBloc {

    override val currentScreen: MutableStateFlow<AppScreen> = MutableStateFlow(
        factory.getTasksDisplayScreen(getTasksDisplayParent())
    )
    private val backStack: MutableList<AppScreen> = mutableListOf(currentScreen.value)

    override fun onClear() {
        backStack.forEach {
            it.onClear()
        }
        backStack.clear()
    }

    /**
     * For [BaseBlocParent] interfaces - objects are used to group methods together.
     * Alternatively [AppBlocImpl] could just implement the interfaces directly.
     */
    private fun getTasksDisplayParent(): TasksDisplayParent = object : TasksDisplayParent {
        override fun navigateToTaskCreate() {
            // TODO: implement
//        backStack.add(currentScreen.value)
//        currentScreen.value = factory.getTaskEditScreen(getTaskEditParent())
        }

        override fun navigateToTaskEdit(task: Task) {
            // TODO: implement
//        backStack.add(currentScreen.value)
//        currentScreen.value = factory.getTaskEditScreen(getTaskEditParent(), task)
        }
    }

}

