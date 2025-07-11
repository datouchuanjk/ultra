plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
android {
    namespace = "io.watermelon.nav"
    compileSdk = COMPILE_SDK
    defaultConfig {
        minSdk = MIN_SDK
    }

    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_11
        targetCompatibility =  JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JVM_TARGET
    }
    buildFeatures {
        compose = true
    }
}
dependencies {
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.navigation.compose)
}

