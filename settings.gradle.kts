rootProject.name = "Task Manager"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    includeBuild("common-build-logic")
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            version("compose", "1.1.1")
            library("compose-ui", "androidx.compose.ui", "ui").versionRef("compose")
            library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").versionRef("compose")
            library("compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").versionRef("compose")
            library("compose-foundation", "androidx.compose.foundation", "foundation").versionRef("compose")
            library("compose-material", "androidx.compose.material", "material").versionRef("compose")
            library("compose-animation", "androidx.compose.animation", "animation").versionRef("compose")
            library("compose-material-icons-core", "androidx.compose.material", "material-icons-core").versionRef("compose")
            bundle("compose", listOf("compose-ui", "compose-ui-tooling", "compose-ui-tooling-preview", "compose-foundation", "compose-material", "compose-animation", "compose-material-icons-core"))

            version("composeActivity", "1.4.0")
            library("composeActivity", "androidx.activity", "activity-compose").versionRef("composeActivity")

            version("coreKtx", "1.7.0")
            library("coreKtx", "androidx.core", "core-ktx").versionRef("coreKtx")

            version("lifecycleViewmodelKtx", "2.4.1")
            library("lifecycleViewmodelKtx", "androidx.lifecycle", "lifecycle-viewmodel-ktx"
            ).versionRef("lifecycleViewmodelKtx")

            version("navigationComponent", "2.4.2")
            library("navigation-fragment-ktx", "androidx.navigation", "navigation-fragment-ktx").versionRef("navigationComponent")
            library("navigation-ui-ktx", "androidx.navigation", "navigation-ui-ktx").versionRef("navigationComponent")
            bundle("navigationComponent", listOf("navigation-fragment-ktx", "navigation-ui-ktx"))

            version("fragmentKtx", "1.4.1")
            library("fragmentKtx", "androidx.fragment", "fragment-ktx").versionRef("fragmentKtx")

            version("room","2.4.2")
            library("room-runtime", "androidx.room", "room-runtime").versionRef("room")
            library("room-ktx", "androidx.room", "room-ktx").versionRef("room")
            bundle("room", listOf("room-runtime", "room-ktx"))

            library("room-compiler", "androidx.room", "room-compiler").versionRef("room")

            version("koin","3.1.6")
            library("koin", "io.insert-koin","koin-android").versionRef("koin")




            library("junit", "junit", "junit").version("4.13.2")
            bundle("testLibs", listOf("junit"))

            library("androidJunit", "androidx.test.ext", "junit").version("1.1.3")
            library("ui-test-junit4", "androidx.compose.ui", "ui-test-junit4").version("1.1.1")
            library("espresso-core", "androidx.test.espresso", "espresso-core").version("3.4.0")
            bundle("androidTestLibs", listOf("androidJunit", "ui-test-junit4", "espresso-core"))
        }
    }
}

include(":app")

rootDir.listFiles()
    .filter { it.name == "features" || it.name == "core" }
    .flatMap { it.listFiles().toList() }
    .filter { it.isDirectory && it.isHidden.not() }
    .forEach { include(":${it.parentFile.name}:${it.name}") }