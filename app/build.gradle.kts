plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}
val default = SigningConfigs.Default()
android {
    namespace = NAMESPACE
    compileSdk = COMPILE_SDK
    defaultConfig {
        applicationId = APPLICATION_ID
        minSdk = MIN_SDK
        targetSdk = TARGET_SDK
    }
    signingConfigs {
        create(default.name) {
            storeFile = file(default.storeFile)
            storePassword = default.storePassword
            keyAlias = default.keyAlias
            keyPassword = default.keyPassword
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName(default.name)
        }
        debug {
            signingConfig = signingConfigs.getByName(default.name)
        }
    }
    flavorDimensions += listOf("default")
    productFlavors {
        create("dev") {
            buildConfigField("String", "BASE_URL", Business.Dev().baseUrl.quoted)
            buildConfigField("Boolean", "IS_DEV", Business.Dev().isDev.toString())
            versionCode = VERSION_CODE
            versionName = VERSION_NAME
            setProperty("archivesBaseName", "VIDI_${versionName}")
        }
        create("prod") {
            buildConfigField("String", "BASE_URL", Business.Dev().baseUrl.quoted)
            buildConfigField("Boolean", "IS_DEV", Business.Dev().isDev.toString())
            versionCode = VERSION_CODE
            versionName = VERSION_NAME
            setProperty("archivesBaseName", "VIDI_${versionName}")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
//    implementation(libs.androidx.lifecycle.process)
    implementation(project(":develop"))
    implementation(project(":paging3"))
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.navigation.compose)
//    implementation(libs.im.chat)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
}