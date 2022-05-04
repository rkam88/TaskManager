plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val libraryExtensionConfig : Action<com.android.build.gradle.LibraryExtension> by rootProject.extra
val composeConfig : Action<com.android.build.gradle.LibraryExtension> by rootProject.extra
android(libraryExtensionConfig)
android(composeConfig)

dependencies {
    implementation(libs.coreKtx)
    implementation(libs.bundles.compose)
}