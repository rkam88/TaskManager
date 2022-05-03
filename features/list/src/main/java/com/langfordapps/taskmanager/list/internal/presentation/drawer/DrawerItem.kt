package com.langfordapps.taskmanager.list.internal.presentation.drawer

import com.langfordapps.taskmanager.list.internal.presentation.ListType

internal sealed class DrawerItem {
    data class ListTypeWithCount(val type: ListType, val count: Int) : DrawerItem()
    object Divider : DrawerItem()
}
