import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.langfordapps.plugins.Constants

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {

    compileSdk = Constants.compileSdk

    defaultConfig {
        applicationId = Constants.applicationId
        minSdk = Constants.minSdk
        targetSdk = Constants.targetSdk

        val majorVersion = 0
        val minorVersion = 3
        val patchVersion = 0
        versionCode = majorVersion * 10_000 + minorVersion * 100 + patchVersion
        versionName = "$majorVersion.$minorVersion.$patchVersion"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    signingConfigs {
        create("release") {
            val localProperties = gradleLocalProperties(rootDir)
            storeFile = file(localProperties.getProperty("storeFile"))
            storePassword = localProperties.getProperty("storePassword")
            keyAlias = localProperties.getProperty("keyAlias")
            keyPassword = localProperties.getProperty("keyPassword")
        }
    }

    compileOptions {
        sourceCompatibility = Constants.jvmCompatibility
        targetCompatibility = Constants.jvmCompatibility
    }

    kotlinOptions {
        jvmTarget = Constants.jvmTarget
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

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