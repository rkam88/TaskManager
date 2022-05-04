import com.android.build.gradle.LibraryExtension

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

val libraryExtensionConfig : Action<LibraryExtension> by rootProject.extra
android(libraryExtensionConfig)

dependencies {
    implementation(libs.coreKtx)
}