package com.langfordapps.taskmanager.tasklist.internal.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.langfordapps.taskmanager.task_storage.api.domain.model.Task
import com.langfordapps.taskmanager.task_storage.api.domain.model.TaskStatus

@Composable
internal fun TaskList(
    tasks: List<Task>,
    onTaskSelected: (Task) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        for (task in tasks) {
            item(key = task.id) {
                TaskElement(
                    task = task,
                    onClick = onTaskSelected,
                )
            }
        }
    }
}

@Composable
private fun TaskElement(
    task: Task,
    onClick: (Task) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(task) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.body1,
            )
        }
        Divider(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Preview
@Composable
private fun TaskListPreview() {
    val tasks = listOf(
        Task(
            id = 0,
            "task without date",
            status = TaskStatus.INBOX,
        ),
        Task(id = 1,
            "task with start, end and alarm",
            status = TaskStatus.INBOX,
            startDate = System.currentTimeMillis(),
            endDate = System.currentTimeMillis(),
            alarmDate = System.currentTimeMillis()),
    )

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        TaskList(
            tasks = tasks,
            onTaskSelected = { },
        )
    }
}