

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
//    id ("org.jetbrains.kotlin.plugin.compose")
    //id("com.google.devtools.ksp")
}

android {
    namespace = "com.zelianko.kitchencalculator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.zelianko.kitchencalculator"
        minSdk = 24
        targetSdk = 35
        versionCode = 23
        versionName = "1.0.0.23"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        kapt {
            arguments{
                arg("room.schemaLocation", "$projectDir")
            }
        }
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
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
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

    implementation ("com.yandex.android:mobileads-mediation:7.11.0.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2025.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.material:material:1.7.8")
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.8")
//    implementation("com.yandex.android:mobmetricalib:5.3.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.3")
    implementation("androidx.lifecycle:lifecycle-process:2.8.7")
    implementation("androidx.databinding:databinding-runtime:8.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.2.0")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.2.0")
   // implementation ("com.yandex.android:mobmetricalib:5.3.0")
    implementation("com.android.billingclient:billing:7.1.1")
    implementation("com.android.billingclient:billing-ktx:7.1.1")
   // implementation ("com.google.android.gms:play-services-ads:22.5.0")
    implementation ("androidx.compose.material:material-icons-extended:1.7.8")
    implementation ("com.github.Breens-Mbaka:Searchable-Dropdown-Menu-Jetpack-Compose:1.1.0")
    //Data store
    implementation("androidx.datastore:datastore-preferences:1.1.3")
    //Dagger hilt
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    //Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation("org.apache.commons:commons-lang3:3.14.0")

    //Google Ads
//    implementation ("com.google.android.gms:play-services-ads:23.0.0")


    implementation ("androidx.window:window:1.3.0")

    // For Java-friendly APIs to register and unregister callbacks
    implementation ("androidx.window:window-java:1.3.0")

    // For RxJava2 integration
    implementation ("androidx.window:window-rxjava2:1.3.0")

    // For RxJava3 integration
    implementation ("androidx.window:window-rxjava3:1.3.0")

    // For testing
    implementation ("androidx.window:window-testing:1.3.0")

    //Meta
//    implementation("com.google.ads.mediation:facebook:6.17.0.0")
//    implementation("com.google.ads.mediation:mintegral:16.7.11.0")
//    implementation("com.google.ads.mediation:vungle:7.3.1.0")

    // AppMetrica SDK.
    implementation("io.appmetrica.analytics:analytics:7.7.0")
}