sealed class Flavors {
    abstract val baseUrl: String
    abstract val isDev: Boolean
    abstract val applicationId: String
    abstract val versionCode: Int
    abstract val versionName: String
    abstract val archivesBaseName: String

    class Dev(
        override val isDev: Boolean = true,
        override val baseUrl: String = "http://8.153.12.37:8185/",
        override val applicationId: String = "com.example.demo",
        override val versionCode: Int = 1,
        override val versionName: String = "1.0.0",
        override val archivesBaseName: String = "Devil_${versionName}",
    ) : Flavors()

    class Prod(
        override val isDev: Boolean = true,
        override val baseUrl: String = "https://api.vidilive.com/",
        override val applicationId: String = "com.example.demo",
        override val versionCode: Int = 1,
        override val versionName: String = "1.0.0",
        override val archivesBaseName: String = "Devil_${versionName}",
    ) : Flavors()

}