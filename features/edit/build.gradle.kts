import com.android.build.gradle.LibraryExtension

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

val libraryExtensionConfig : Action<LibraryExtension> by rootProject.extra
val composeConfig : Action<LibraryExtension> by rootProject.extra
android(libraryExtensionConfig)
android(composeConfig)

dependencies {
    implementation(project(":core:compose"))
    implementation(libs.bundles.compose)
    implementation(libs.coreKtx)
    implementation(libs.lifecycleViewmodelKtx)
    implementation(libs.fragmentKtx)
}