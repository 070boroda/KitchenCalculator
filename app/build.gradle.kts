

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.zelianko.kitchencalculator"
    compileSdk = 35

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    defaultConfig {
        applicationId = "com.zelianko.kitchencalculator"
        minSdk = 29
        targetSdk = 35
        versionCode = 30
        versionName = "1.0.0.30"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.addAll(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
            )
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation ("com.yandex.android:mobileads-mediation:7.14.1.0")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2025.07.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.material:material:1.8.3")
    implementation("androidx.navigation:navigation-compose:2.9.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.8.3")
//    implementation("com.yandex.android:mobmetricalib:5.3.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.5")
    implementation("androidx.lifecycle:lifecycle-process:2.9.2")
    implementation("androidx.databinding:databinding-runtime:8.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.07.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.3.0")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.3.0")
   // implementation ("com.yandex.android:mobmetricalib:5.3.0")
    implementation("com.android.billingclient:billing:7.1.1")
    implementation("com.android.billingclient:billing-ktx:7.1.1")
   // implementation ("com.google.android.gms:play-services-ads:22.5.0")
    implementation ("androidx.compose.material:material-icons-extended:1.7.8")
    implementation ("com.github.Breens-Mbaka:Searchable-Dropdown-Menu-Jetpack-Compose:1.1.0")
    //Data store
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    //Dagger hilt
    implementation("com.google.dagger:hilt-android:2.57")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("com.google.dagger:hilt-compiler:2.57")
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    //Room
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")

    //Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation("org.apache.commons:commons-lang3:3.18.0")

    //Google Ads
//    implementation ("com.google.android.gms:play-services-ads:23.0.0")


    implementation ("androidx.window:window:1.4.0")

    // For Java-friendly APIs to register and unregister callbacks
    implementation ("androidx.window:window-java:1.4.0")

    // For RxJava2 integration
    implementation ("androidx.window:window-rxjava2:1.4.0")

    // For RxJava3 integration
    implementation ("androidx.window:window-rxjava3:1.4.0")

    // For testing
    implementation ("androidx.window:window-testing:1.4.0")

    //Meta
//    implementation("com.google.ads.mediation:facebook:6.17.0.0")
//    implementation("com.google.ads.mediation:mintegral:16.7.11.0")
//    implementation("com.google.ads.mediation:vungle:7.3.1.0")

    // AppMetrica SDK.
    implementation("io.appmetrica.analytics:analytics:7.10.0")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
    implementation("com.google.firebase:firebase-analytics")
}