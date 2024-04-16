import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    //id("com.google.devtools.ksp")
}

android {
    namespace = "com.zelianko.kitchencalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zelianko.kitchencalculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 6
        versionName = "1.0.0.6"

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
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

  //  implementation ("com.yandex.android:mobileads:6.1.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.compose.material:material:1.5.4")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.4")
    implementation("com.yandex.android:mobmetricalib:5.3.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.9.9")
    implementation("androidx.lifecycle:lifecycle-process:2.7.0")
    implementation("androidx.databinding:databinding-runtime:8.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.2.0")
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.2.0")
   // implementation ("com.yandex.android:mobmetricalib:5.3.0")
    implementation("com.android.billingclient:billing:6.2.0")
    implementation("com.android.billingclient:billing-ktx:6.2.0")
   // implementation ("com.google.android.gms:play-services-ads:22.5.0")
    implementation ("androidx.compose.material:material-icons-extended:1.5.4")
    implementation ("com.github.Breens-Mbaka:Searchable-Dropdown-Menu-Jetpack-Compose:0.3.6")
    //Data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")
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

    implementation("org.apache.commons:commons-lang3:3.0")

    //Google Ads
    implementation ("com.google.android.gms:play-services-ads:23.0.0")


    implementation ("androidx.window:window:1.2.0")

    // For Java-friendly APIs to register and unregister callbacks
    implementation ("androidx.window:window-java:1.2.0")

    // For RxJava2 integration
    implementation ("androidx.window:window-rxjava2:1.2.0")

    // For RxJava3 integration
    implementation ("androidx.window:window-rxjava3:1.2.0")

    // For testing
    implementation ("androidx.window:window-testing:1.2.0")
}