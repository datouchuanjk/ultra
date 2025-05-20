package io.datou.develop

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Process
import androidx.core.content.getSystemService
import java.lang.ref.WeakReference
import java.lang.reflect.Proxy

val AppContext: Context
    get() = checkNotNull(ContextManager.appContext)

val TopActivityOrNull: Activity?
    get() = ContextManager.topActivity?.get()

fun Application.startDevelop() {
    if (isMainProcess) {
        ContextManager.init(this)
    }
}

internal object ContextManager {

    var topActivity: WeakReference<Activity>? = null

    var appContext: Context? = null

    fun init(application: Application) {
        appContext = application
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks by noOpProxyDelegate() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity != topActivity?.get()) {
                    topActivity = WeakReference(activity)
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activity == topActivity?.get()) {
                    topActivity = null
                }
            }
        }
        )
    }

    inline fun <reified T : Any> noOpProxyDelegate(): T {
        val javaClass = T::class.java
        return Proxy.newProxyInstance(
            javaClass.classLoader, arrayOf(javaClass)
        ) { _, _, _ -> } as T
    }
}



