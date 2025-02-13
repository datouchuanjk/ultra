
const val TARGET_SDK = 35
const val MIN_SDK = 24
const val COMPILE_SDK = 35
const val NAMESPACE = "com.example.demo"
const val APPLICATION_ID = "com.example.demo"
const val VERSION_CODE = 1
const val VERSION_NAME = "1.0.0"

/**
 * jks 配置
 */
sealed class SigningConfigs {
    abstract val name: String
    abstract val storeFile: String
    abstract val storePassword: String
    abstract val keyAlias: String
    abstract val keyPassword: String

    class Default(
        override val name: String = "default",
        override val storeFile: String = "default.jks",
        override val storePassword: String = "123456",
        override val keyAlias: String = "key0",
        override val keyPassword: String = "123456"
    ) : SigningConfigs()
}




