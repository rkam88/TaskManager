package com.langfordapps.taskmanager.core.coroutines

import kotlinx.coroutines.Dispatchers

object DispatchersProvider {
    val Main = Dispatchers.Main
    val IO = Dispatchers.IO
    val Default = Dispatchers.Default
}

///**
// * For unit-tests. Place in test package.
// */
//object DispatchersProvider {
//    val testCoroutineDispatcher = TestCoroutineDispatcher()
//    val Main = testCoroutineDispatcher
//    val IO = testCoroutineDispatcher
//    val Default = testCoroutineDispatcher
//}