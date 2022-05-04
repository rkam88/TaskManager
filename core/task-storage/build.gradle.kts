plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

val libraryExtensionConfig : Action<com.android.build.gradle.LibraryExtension> by rootProject.extra
android(libraryExtensionConfig)

dependencies {
    implementation(libs.coreKtx)
    implementation(libs.bundles.room)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.koin)
}