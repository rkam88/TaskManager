import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    id("com.android.application") version "7.1.1" apply false
    id("com.android.library") version "7.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("com.google.devtools.ksp") version "1.6.10-1.0.2" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

private object Constants {
    const val compileSdk = 32
    const val minSdk = 23
    const val targetSdk = 32
    val javaCompatibility = JavaVersion.VERSION_11
    const val jvmTarget = "11"
}

ext {
    extra["appExtensionConfig"] =Action<BaseAppModuleExtension> {
        compileSdk = Constants.compileSdk

        defaultConfig {
            applicationId = "com.langfordapps.taskmanager"
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
                val localProperties =
                    com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)
                storeFile = file(localProperties.getProperty("storeFile"))
                storePassword = localProperties.getProperty("storePassword")
                keyAlias = localProperties.getProperty("keyAlias")
                keyPassword = localProperties.getProperty("keyPassword")
            }
        }

        compileOptions {
            sourceCompatibility = Constants.javaCompatibility
            targetCompatibility = Constants.javaCompatibility
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

    extra["libraryExtensionConfig"] = Action<LibraryExtension> {
        compileSdk = Constants.compileSdk

        defaultConfig {
            minSdk = Constants.minSdk
            targetSdk = Constants.targetSdk

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

        compileOptions {
            sourceCompatibility = Constants.javaCompatibility
            targetCompatibility = Constants.javaCompatibility
        }

        kotlinOptions {
            jvmTarget = Constants.jvmTarget
        }

        packagingOptions {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }

    extra["composeConfig"] = Action<LibraryExtension> {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.compose.get()
        }
    }
}

fun BaseExtension.kotlinOptions(configure: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", configure)
}
