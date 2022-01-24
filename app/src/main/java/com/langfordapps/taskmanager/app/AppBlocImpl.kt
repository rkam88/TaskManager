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

    private val blocScope = CoroutineScope(SupervisorJob() + DispatchersProvider.Main)

    private val _action = MutableSharedFlow<AppAction>()
    override val action: Flow<AppAction> = _action.asSharedFlow()
    private fun sendAction(action: AppAction) {
        blocScope.launch { _action.emit(action) }
    }

    private val backStack: MutableList<AppScreen> = mutableListOf(
        factory.getTasksDisplayScreen(getTasksDisplayParent())
    )
    override val currentScreen: MutableStateFlow<AppScreen> = MutableStateFlow(backStack.last())

    override fun onClear() {
        blocScope.cancel()
        backStack.forEach { appScreen -> appScreen.onClear() }
        backStack.clear()
    }

    override fun onBackPressed() {
        when (backStack.size) {
            1 -> sendAction(AppAction.HandleBackPressBySystem)
            else -> popBackStack()
        }
    }

    private fun popBackStack() {
        backStack.last().onClear()
        backStack.removeLast()
        currentScreen.update { backStack.last() }
    }

    private fun pushToBackStack(screen: AppScreen) {
        backStack.add(screen)
        currentScreen.update { backStack.last() }
    }

    /**
     * For [BaseBlocParent] interfaces - objects are used to group methods together.
     * Alternatively [AppBlocImpl] could just implement the interfaces directly.
     */
    private fun getTasksDisplayParent(): TasksDisplayParent = object : TasksDisplayParent {
        override fun navigateToTaskCreate() {
            // TODO: implement
//            pushToBackStack(factory.getTaskEditScreen(getTaskEditParent()))
        }

        override fun navigateToTaskEdit(task: Task) {
            // TODO: implement
//            pushToBackStack(factory.getTaskEditScreen(getTaskEditParent(), task))
        }
    }

}

