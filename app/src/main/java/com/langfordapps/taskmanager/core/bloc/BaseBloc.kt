package com.langfordapps.taskmanager.core.bloc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Used to define the current state of the View.
 */
interface BaseState

/**
 * Something that happened that affects the current Bloc.
 * Used to represent the possible interactions of the User, operating system or parent
 * with the Bloc.
 * Examples could include: OnSomeButtonPressed, OnSomeOtherScreenResult, OnPermissionGranted.
 *
 * Note: this could be split up in different interfaces (ViewEvent, ParentEvent),
 * but having just one seems a better choice at the moment - KISS & YAGNI.
 */
interface BaseEvent

/**
 * Single-shot actions to be performed/handled by the View.
 * Examples: showing a dialog, requesting a permission, etc.
 */
interface BaseAction

/**
 * An interface the parent Bloc must implement, so that the child can communicate with it.
 * Can be used for navigating to other screens or to inform the parent of changes it should
 * or could react to.
 *
 * An alternative way to organize child-to-parent communication would be
 * to use a ParentAction interface and a Flow<ParentAction> the parent can subscribe to.
 */
interface BaseBlocParent

/**
 * A Finite State Machine interface for Blocs
 */
interface BaseBloc<State, Event, Action, Parent> : Bloc
        where State : BaseState,
              Event : BaseEvent,
              Action : BaseAction,
              Parent : BaseBlocParent {

    val state: StateFlow<State>

    fun onNewEvent(event: Event)

    val action: Flow<Action>

    val parent: Parent

    fun cancelCurrentJobs()

}