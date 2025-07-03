plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}
android {
    namespace = "com.module.main"
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
    implementation(project(":module_basic"))
    implementation(project(":module_home"))
    implementation(project(":module_mine"))
    implementation(project(":module_chat"))
    // ⬇️  Hilt 依赖
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
}