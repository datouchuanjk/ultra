plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.ksp)
}
val defaultSigningConfig = SigningConfigs.Default()
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

    signingConfigs {
        create(defaultSigningConfig.name) {
            storeFile = file(defaultSigningConfig.storeFile)
            storePassword = defaultSigningConfig.storePassword
            keyAlias = defaultSigningConfig.keyAlias
            keyPassword = defaultSigningConfig.keyPassword
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName(defaultSigningConfig.name)
        }
        debug {
            signingConfig = signingConfigs.getByName(defaultSigningConfig.name)
        }
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
        buildConfig = true
    }
}

dependencies {
    implementation(project(":module_basic"))
    implementation(project(":module_main"))
    implementation(project(":module_home"))
    implementation(project(":module_mine"))
    implementation(project(":module_chat"))
    implementation(project(":module_login"))
}

