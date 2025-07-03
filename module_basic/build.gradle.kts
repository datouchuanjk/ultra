plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}
android {
    namespace = "com.module.basic"
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
//    basic
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
//    compose
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.navigation.compose)
    api(libs.coil.compose)
    api(libs.androidx.constraintlayout.compose)
//    retrofit
    api(libs.retrofit)
    api(libs.okhttp)
    api(libs.logging.interceptor)
    api(libs.converter.gson)
    // hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    //X
    api(project(":ComposeX"))
}