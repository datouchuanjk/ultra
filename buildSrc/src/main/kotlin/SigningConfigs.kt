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
