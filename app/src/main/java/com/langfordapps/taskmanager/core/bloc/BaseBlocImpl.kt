package com.langfordapps.taskmanager.core.bloc

import androidx.annotation.CallSuper
import com.langfordapps.taskmanager.core.coroutines.DispatchersProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@Suppress("PropertyName", "unused")
abstract class BaseBlocImpl<State, Event, Action, Parent>(
    initialState: State,
) : BaseBloc<State, Event, Action, Parent> where State : BaseState,
                                                 Event : BaseEvent,
                                                 Action : BaseAction,
                                                 Parent : BaseBlocParent {

    protected val _state = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state.asStateFlow()
    protected inline fun updateState(function: (State) -> State) = _state.update(function)

    abstract override fun onNewEvent(event: Event)

    private val _action = MutableSharedFlow<Action>()
    override val action: Flow<Action> = _action.asSharedFlow()
    protected fun sendAction(action: Action) {
        coroutineScope.launch { _action.emit(action) }
    }

    abstract override val parent: Parent

    @Suppress("MemberVisibilityCanBePrivate")
    protected val coroutineScope = CoroutineScope(
        SupervisorJob() + DispatchersProvider.Main
    )

    @CallSuper
    override fun onClear() {
        coroutineScope.cancel()
    }

    @CallSuper
    override fun cancelCurrentJobs() {
        requireNotNull(coroutineScope.coroutineContext[Job.Key]).cancelChildren()
    }

}