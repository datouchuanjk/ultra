plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "io.watermelon.paging"
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
}