package com.langfordapps.taskmanager.tasklist.internal.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.langfordapps.taskmanager.task_storage.api.domain.model.Task
import com.langfordapps.taskmanager.task_storage.api.domain.model.TaskStatus
import com.langfordapps.taskmanager.tasklist.internal.presentation.drawer.Drawer
import com.langfordapps.taskmanager.tasklist.internal.presentation.drawer.DrawerItem
import com.langfordapps.taskmanager.tasklist.internal.presentation.drawer.DrawerItem.Item
import com.langfordapps.taskmanager.tasklist.internal.presentation.list.TaskList
import kotlinx.coroutines.launch

@Composable
internal fun TaskListScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val onNavigationIconClicked: () -> Unit = {
        scope.launch {
            scaffoldState.drawerState.apply { if (isClosed) open() else close() }
        }
    }


    /* TODO: move to VM state */
    var listType: TaskListType by remember { mutableStateOf(TaskListType.Inbox) }
    var drawerItems: List<DrawerItem> by remember {
        mutableStateOf(listOf(
            Item(TaskListType.Inbox, 1),
            DrawerItem.Divider,
            Item(TaskListType.NextActions, 1),
            Item(TaskListType.Calendar, 1),
            DrawerItem.Divider,
            Item(TaskListType.WaitingFor, 1),
            Item(TaskListType.SomedayMaybe, 1),
            DrawerItem.Divider,
            Item(TaskListType.Completed, 1),
        ))
    }
    val onDrawerItemClicked: (TaskListType) -> Unit = {
        listType = it
        scope.launch { scaffoldState.drawerState.close() }
    }
    val tasks: List<Task> = listOf(
        Task(1, "task 1", TaskStatus.INBOX),
        Task(2, "task 2", TaskStatus.INBOX),
    )
    val context = LocalContext.current
    val onTaskSelected: (Task) -> Unit = {
        Toast.makeText(context, "${it.name} - clicked!", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            Toolbar(
                text = stringResource(id = listType.titleRes),
                onNavigationIconClicked = onNavigationIconClicked
            )
        },
        floatingActionButton = { Fab(onClick = { /* TODO: handle FAB with listType */ }) },
        drawerContent = { Drawer(items = drawerItems, onItemSelected = onDrawerItemClicked) },
        scaffoldState = scaffoldState,
    ) { contentPadding ->
        Box(modifier = Modifier.padding(paddingValues = contentPadding)) {
            TaskList(
                tasks = tasks,
                onTaskSelected = onTaskSelected,
            )
        }
    }
}

@Composable
private fun Toolbar(text: String, onNavigationIconClicked: () -> Unit) {
    TopAppBar(
        title = { Text(text = text) },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClicked,
                content = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                    )
                }
            )
        },
    )
}

@Composable
private fun Fab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        content = { Icon(imageVector = Icons.Default.Add, contentDescription = null) }
    )
}


@Preview
@Composable
private fun ListScreenPreview() {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        TaskListScreen()
    }
}