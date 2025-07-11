plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
    id("signing")
}
android {
    namespace = "io.watermelon.banner"
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
    implementation(libs.androidx.compose.material3)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "io.github.datouchuanjk"
            artifactId = "banner"
            version = "1.0.0"
            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            url = uri("https://your-maven-repo/releases")
            credentials {
                username = project.findProperty("mavenUsername") as String? ?: ""
                password = project.findProperty("mavenPassword") as String? ?: ""
            }
        }
    }
}
