sealed class Business {
    abstract val baseUrl: String
    abstract val isDev: Boolean


     class Dev(
        override val isDev: Boolean = true,
        override val baseUrl: String = "http://8.153.12.37:8185/",
    ) : Business()


     class Prod(
        override val isDev: Boolean = true,
        override val baseUrl: String = "https://api.vidilive.com/",
    ) : Business()

}