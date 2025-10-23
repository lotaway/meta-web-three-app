pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("android", "8.13.0")
            version("kotlin", "2.1.20")
            version("dagger-hilt", "2.44")
            version("lifecycle", "2.6.0-rc01")
//            library("android-gradle-plugin", "com.android.tools.build", "gradle").versionRef("agp")
            plugin(
                "androidApplication",
                "com.android.application"
            ).versionRef("android")
            plugin(
                "androidLibrary",
                "com.android.library"
            ).versionRef("android")
            plugin(
                "kotlinAndroid",
                "org.jetbrains.kotlin.android"
            ).versionRef("kotlin")
            plugin(
                "daggerHiltAndroid",
                "com.google.dagger.hilt.android"
            ).versionRef("dagger-hilt")
            library(
                "daggerHilt",
                "com.google.dagger",
                "hilt-android"
            ).versionRef("dagger-hilt")
            library(
                "lifecycleRuntimeKtx",
                "androidx.lifecycle",
                "lifecycle-runtime-ktx"
            ).versionRef("lifecycle")
            library(
                "lifecycleRuntimeCompose",
                "androidx.lifecycle",
                "lifecycle-runtime-compose"
            ).versionRef("lifecycle")
        }
    }
}
rootProject.name = "MetaWebThreeAndroid"
include(":app")
