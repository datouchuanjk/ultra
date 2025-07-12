import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.vanniktech.maven.publish") version "0.32.0"
}
android {
    namespace = "io.watermelon.banner"
    compileSdk = COMPILE_SDK
    defaultConfig {
        minSdk = MIN_SDK
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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

mavenPublishing{
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("io.github.datouchuanjk", "banner", "1.0.0")
    pom{
        name.set("io.github.datouchuanjk")
        description.set("banner")
        inceptionYear.set("2025")
        url.set("https://gitee.com/datouchuanjk/Develop.git")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("datouchuanjk")
                name.set("datouchuanjk")
                email.set("13720327027@163.com")
                url.set("https://gitee.com/datouchuanjk")
            }
        }
        scm {
            url.set("https://gitee.com/datouchuanjk/develop/tree/master/banner")
            connection.set("scm:git:https://gitee.com/datouchuanjk/Develop.git")
            developerConnection.set("scm:git:git@gitee.com:datouchuanjk/Develop.git")
        }
    }
}



