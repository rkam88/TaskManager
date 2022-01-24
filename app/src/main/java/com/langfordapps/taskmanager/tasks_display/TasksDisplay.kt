package com.langfordapps.taskmanager.tasks_display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.langfordapps.taskmanager.common.domain.Task

@Composable
fun TasksDisplay(bloc: TasksDisplayBloc) {
    val state by bloc.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "List of tasks:",
                modifier = Modifier.padding(bottom = 16.dp),
                fontSize = 24.sp,
            )
        }
        items(state.taskList) { task ->
            Task(
                task = task,
                OnMarkedAsComplete = { bloc.onNewEvent(TasksDisplayEvent.OnMarkedAsComplete(task)) },
                onDeleteClicked = { bloc.onNewEvent(TasksDisplayEvent.OnDeleteClicked(task)) },
            )
        }
    }
}

@Composable
private fun Task(
    task: Task,
    OnMarkedAsComplete: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = task.id.toString())
        Text(text = task.name)
        if (task.isCompleted.not()) {
            Button(onClick = OnMarkedAsComplete) {
                Text(text = "Mark as complete")
            }
        }
        Button(onClick = onDeleteClicked) {
            Text(text = "Delete task")
        }
    }
}