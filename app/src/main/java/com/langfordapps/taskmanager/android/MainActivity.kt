package com.langfordapps.taskmanager.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.langfordapps.taskmanager.app.AppAction
import com.langfordapps.taskmanager.app.AppScreen
import com.langfordapps.taskmanager.app.AppScreen.TasksDisplayScreen
import com.langfordapps.taskmanager.core.extensions.exhaustive
import com.langfordapps.taskmanager.tasks_display.TasksDisplay
import com.langfordapps.taskmanager.ui.theme.TaskManagerTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initObservers()

        setContent {
            TaskManagerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val appScreenState by viewModel.currentScreen.collectAsState()
                    TaskManagerApp(screen = appScreenState)
                }
            }
        }
    }

    @Composable
    private fun TaskManagerApp(screen: AppScreen) {
        when (screen) {
            is TasksDisplayScreen -> TasksDisplay(screen.bloc)
        }.exhaustive
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.action
                .flowWithLifecycle(lifecycle)
                .collect { action -> onNewAction(action) }
        }
    }

    private fun onNewAction(action: AppAction) {
        when (action) {
            AppAction.HandleBackPressBySystem -> super.onBackPressed()
        }.exhaustive
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}