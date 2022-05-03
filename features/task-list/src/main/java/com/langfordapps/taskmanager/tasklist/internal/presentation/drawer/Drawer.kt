package com.langfordapps.taskmanager.tasklist.internal.presentation.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.langfordapps.taskmanager.common.api.extensions.exhaustive
import com.langfordapps.taskmanager.tasklist.internal.presentation.TaskListType

@Composable
internal fun Drawer(
    items: List<DrawerItem>,
    onItemSelected: (TaskListType) -> Unit,
) {
    Column {
        for (item in items) {
            when (item) {
                is DrawerItem.ListTypeWithCount -> ListTypeWithCount(item, onItemSelected)
                DrawerItem.Divider -> Divider()
            }.exhaustive
        }
    }
}

@Composable
private fun ListTypeWithCount(
    item: DrawerItem.ListTypeWithCount,
    onItemSelected: (TaskListType) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onItemSelected(item.type) })
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = item.type.titleRes),
            modifier = Modifier
                .padding(end = 24.dp)
                .weight(1f)
        )
        Text(
            text = item.count.toString(),
        )
    }
}