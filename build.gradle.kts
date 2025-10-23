// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    id("com.android.application") version "8.13.0" apply false
//    id("com.android.library") version "8.13.0" apply false
//    id("org.jetbrains.kotlin.android") version "2.1.20" apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
//    kotlin("android") version "2.0.20-Beta1" apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.daggerHiltAndroid) apply false
}