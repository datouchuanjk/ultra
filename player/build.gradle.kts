plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "io.watermelon.player"
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.media3.exoplayer)
    api(libs.androidx.media3.exoplayer.hls)
    api(libs.androidx.media3.exoplayer.dash)
}