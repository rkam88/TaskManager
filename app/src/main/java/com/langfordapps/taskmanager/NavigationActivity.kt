package com.langfordapps.taskmanager

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController

class MainActivity : FragmentActivity() {

    private val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
    }

}