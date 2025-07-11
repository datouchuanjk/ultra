plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}
android {
    namespace = NAMESPACE
    compileSdk = COMPILE_SDK
    defaultConfig {
        targetSdk = TARGET_SDK
        minSdk = MIN_SDK
        applicationId = APPLICATION_ID
        versionCode = VERSION_CODE
        versionName = VERSION_NAME
    }


    buildTypes {
        release {
            isMinifyEnabled = false
        }
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
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(project(":paging"))
    implementation(project(":wheel"))
    implementation(project(":sticky"))
    implementation(project(":player"))
    implementation(project(":nav"))
    implementation(project(":banner"))
}

