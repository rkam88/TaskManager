package com.langfordapps.taskmanager.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.langfordapps.taskmanager.app.AppAction
import com.langfordapps.taskmanager.core.extensions.exhaustive
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
                    val appScreenState = viewModel.currentScreen.collectAsState()

                    Greeting("state - $appScreenState")
                }
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.action
                .flowWithLifecycle(lifecycle)
                .collect(::onNewAction)
        }
    }

    private fun onNewAction(action: AppAction) {
        when (action) {
            AppAction.OnBackPressed -> super.onBackPressed()
        }.exhaustive
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TaskManagerTheme {
        Greeting("Android")
    }
}