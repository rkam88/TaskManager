package com.langfordapps.plugins

import org.gradle.api.JavaVersion

object Constants {
    const val compileSdk = 32
    const val minSdk = 23
    const val targetSdk = 32
    const val applicationId = "com.langfordapps.taskmanager"
    const val jvmTarget = "11"
    val jvmCompatibility = JavaVersion.VERSION_11
}