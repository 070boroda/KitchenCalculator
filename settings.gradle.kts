pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.google.devtools.ksp") version "1.9.22-1.0.17" // Актуальная версия KSP
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        }
        // IronSource
        maven {
            url = uri("https://android-sdk.is.com/")
        }

// Pangle
        maven {
            url = uri("https://artifact.bytedance.com/repository/pangle")
        }

// Tapjoy
        maven {
            url = uri("https://sdk.tapjoy.com/")
        }

// Chartboost
        maven {
            url = uri("https://cboost.jfrog.io/artifactory/chartboost-ads/")
        }

// AppNext
        maven {
            url = uri("https://dl.appnext.com/")
        }
    }
}

rootProject.name = "KitchenCalculator"
include(":app")
 