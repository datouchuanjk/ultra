plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
val defaultSigningConfig = SigningConfigs.Default()
android {
    namespace = NAMESPACE
    compileSdk = COMPILE_SDK
    defaultConfig {
        targetSdk = TARGET_SDK
        minSdk = MIN_SDK
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
    flavorDimensions += listOf("default")
    productFlavors {
        create("dev") {
            val dev = Flavors.Dev()
            buildConfigField("String", "BASE_URL", dev.baseUrl.quoted)
            buildConfigField("Boolean", "IS_DEV", dev.isDev.toString())
            applicationId = dev.applicationId
            versionCode = dev.versionCode
            versionName = dev.versionName
            setProperty("archivesBaseName", dev.archivesBaseName)
        }
        create("prod") {
            val prod = Flavors.Prod()
            buildConfigField("String", "BASE_URL", prod.baseUrl.quoted)
            buildConfigField("Boolean", "IS_DEV", prod.isDev.toString())
            applicationId = prod.applicationId
            versionCode = prod.versionCode
            versionName = prod.versionName
            setProperty("archivesBaseName", prod.archivesBaseName)
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(project(":develop"))
    implementation(project(":chat"))
    implementation(libs.im.chat)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.constraintlayout.compose)
}