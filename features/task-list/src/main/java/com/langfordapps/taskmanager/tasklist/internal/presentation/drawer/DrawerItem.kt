package com.langfordapps.taskmanager.tasklist.internal.presentation.drawer

import com.langfordapps.taskmanager.tasklist.internal.presentation.TaskListType

internal sealed class DrawerItem {
    data class Item(val type: TaskListType, val count: Int) : DrawerItem()
    object Divider : DrawerItem()
}
