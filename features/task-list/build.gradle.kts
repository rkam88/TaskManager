plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {

    compileSdk = 32

    defaultConfig {
        minSdk = 23
        targetSdk = 32

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(project(":core:common"))
    implementation(project(":core:compose"))
    implementation(project(":core:task-storage"))
    implementation(libs.bundles.compose)
    implementation(libs.coreKtx)
    implementation(libs.lifecycleViewmodelKtx)
    implementation(libs.fragmentKtx)
}