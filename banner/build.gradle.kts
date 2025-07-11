import org.gradle.internal.impldep.com.jcraft.jsch.JSch.setConfig

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

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "io.github.datouchuanjk"
            artifactId = "banner"
            version = "1.0.0"
            afterEvaluate {
                from(components["release"])
            }
            pom {
                name.set(" Banner ")
                description.set("A reusable banner component for Android applications")
                url.set("https://gitee.com/datouchuanjk/develop/tree/master/banner")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("datouchuanjk")
                        name.set("datouchuanjk")
                        email.set("13720327027@163.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://gitee.com/datouchuanjk/Develop.git")
                    developerConnection.set("scm:git:git@gitee.com:datouchuanjk/Develop.git")
                    url.set("https://gitee.com/datouchuanjk/develop/tree/master/banner")
                }
            }
        }
    }
    repositories {
        maven {
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = "zaskSVX8"
                password = "C1q4QjzojZ0u3LcYHkP8CzlpCSIJLl2axqkObyaTR+3i"
            }
        }
    }
}

signing {
    sign(publishing.publications["release"])
}
