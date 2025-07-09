plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
android {
    namespace = "io.composex"
    compileSdk = COMPILE_SDK

    defaultConfig {
        minSdk = MIN_SDK
    }

    compileOptions {
        sourceCompatibility = SOURCE_COMPATIBILITY
        targetCompatibility = TARGET_COMPATIBILITY
    }
    kotlinOptions {
        jvmTarget = JVM_TARGET
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    //⬇️  Basic
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    // ⬇️  Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)

    // ⬇️  Exoplayer
    api(libs.androidx.media3.exoplayer)
    api(libs.androidx.media3.exoplayer.hls)
    api(libs.androidx.media3.exoplayer.dash)

    // ⬇️  datastore
    api(libs.androidx.datastore.preferences)
}
