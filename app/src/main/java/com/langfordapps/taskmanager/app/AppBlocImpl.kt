package com.langfordapps.taskmanager.app

import com.langfordapps.taskmanager.common.domain.Task
import com.langfordapps.taskmanager.core.bloc.BaseBlocParent
import com.langfordapps.taskmanager.core.coroutines.DispatchersProvider
import com.langfordapps.taskmanager.tasks_display.TasksDisplayParent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppBlocImpl(
    private val factory: AppScreenFactory
) : AppBloc {

    private val coroutineScope = CoroutineScope(SupervisorJob() + DispatchersProvider.Main)
    private val _action = MutableSharedFlow<AppAction>()
    override val action: Flow<AppAction> = _action.asSharedFlow()

    override val currentScreen: MutableStateFlow<AppScreen> = MutableStateFlow(
        factory.getTasksDisplayScreen(getTasksDisplayParent())
    )

    private val backStack: MutableList<AppScreen> = mutableListOf(currentScreen.value)

    private fun sendAction(action: AppAction) {
        coroutineScope.launch { _action.emit(action) }
    }

    override fun onBackPressed() {
        if (backStack.size >= 1) {
            backStack.removeAt(backStack.lastIndex)
            currentScreen.update { backStack.last() }
        } else {
            sendAction(AppAction.OnBackPressed)
        }
    }

    override fun onClear() {
        backStack.forEach { appScreen -> appScreen.onClear() }
        backStack.clear()
        coroutineScope.cancel()
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

