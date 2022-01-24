package com.langfordapps.taskmanager.core.bloc

/**
 * A BLoC (Business Logic Component) represents a part of the app: either a screen
 * or some part of it.
 *
 * In general, a Bloc will:
 * - contain a state for the view to render
 * - be able to emit one shot-actions to be handled by the view (showing dialogs, requesting permissions)
 * - be able to communicate with its parent (for example, for navigation)
 * - handle events from the view and the parent Bloc.
 * All this can be done in several ways, so the base interface does not force you into any
 * approach.
 *
 * The only requirement is a method - [onClear], that the parent can invoke to stop the
 * execution of any long-term jobs in the background when the Bloc is not needed anymore.
 */
interface Bloc {
    fun onClear()
}