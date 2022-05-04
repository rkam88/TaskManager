import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val appExtensionConfig: Action<BaseAppModuleExtension> by rootProject.extra
android(appExtensionConfig)

dependencies {
    rootDir.listFiles()
        .filter { it.name == "features" || it.name == "core" }
        .flatMap { it.listFiles().toList() }
        .filter { it.isDirectory && it.isHidden.not() }
        .forEach { implementation(project((":${it.parentFile.name}:${it.name}"))) }

    implementation(libs.bundles.compose)
    implementation(libs.coreKtx)
    implementation(libs.composeActivity)
    implementation(libs.lifecycleViewmodelKtx)
    implementation(libs.bundles.navigationComponent)
    implementation(libs.koin)

    testImplementation(libs.bundles.testLibs)
    androidTestImplementation(libs.bundles.androidTestLibs)
}