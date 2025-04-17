plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"

}

android {
    namespace = "com.frontend.mobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.frontend.mobile"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }


    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0" // or the latest stable version
    }



}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material:material:1.5.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")


    implementation ("androidx.compose.material:material-icons-extended:1.5.0")

    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp for handling networking
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Coroutine Support for Retrofit
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    implementation ("androidx.navigation:navigation-compose:2.6.0")



    implementation ("androidx.navigation:navigation-compose:2.6.0") // Or the latest version
    implementation ("androidx.compose.ui:ui:1.4.0") // Ensure you have this for Compose UI
    implementation ("androidx.compose.material:material:1.4.0") // Material UI components
    implementation ("androidx.compose.ui:ui-tooling-preview:1.4.0")

    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    // Compose UI and Material
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material") // for classic Material components
    implementation("androidx.compose.material3:material3") // for Material 3 components
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Icons
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // Other (optional)
    implementation("androidx.activity:activity-compose:1.8.2")


}