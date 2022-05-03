package com.langfordapps.taskmanager.list.internal.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.langfordapps.taskmanager.list.internal.presentation.drawer.Drawer
import com.langfordapps.taskmanager.list.internal.presentation.drawer.DrawerItem
import com.langfordapps.taskmanager.list.internal.presentation.drawer.DrawerItem.ListTypeWithCount
import kotlinx.coroutines.launch

@Composable
internal fun ListScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val onNavigationIconClicked: () -> Unit = {
        scope.launch {
            scaffoldState.drawerState.apply { if (isClosed) open() else close() }
        }
    }


    /* TODO: move to VM state */
    var listType: ListType by remember { mutableStateOf(ListType.Inbox) }
    var drawerItems: List<DrawerItem> by remember {
        mutableStateOf(listOf(
            ListTypeWithCount(ListType.Inbox, 1),
            DrawerItem.Divider,
            ListTypeWithCount(ListType.NextActions, 1),
            ListTypeWithCount(ListType.Calendar, 1),
            DrawerItem.Divider,
            ListTypeWithCount(ListType.WaitingFor, 1),
            ListTypeWithCount(ListType.SomedayMaybe, 1),
            DrawerItem.Divider,
            ListTypeWithCount(ListType.Completed, 1),
        ))
    }
    val onDrawerItemClicked: (ListType) -> Unit = {
        listType = it
        scope.launch { scaffoldState.drawerState.close() }
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
        // TODO: display tasks
        Box(modifier = Modifier.padding(paddingValues = contentPadding)) {
            Text("$listType")
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
        ListScreen()
    }
}