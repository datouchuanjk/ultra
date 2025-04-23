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

fun Application.startDevelop() {
    if (isMainProcess) {
        ContextManager.init(this)
    }
}

val AppContext: Application get() {
    val result = ContextManager.application
    require(result!=null){
        "invoke startDevelop in  your Application first"
    }
    return result
}

val TopActivityOrNull get() = ContextManager.topActivity?.get()

val TopActivity get() = checkNotNull(TopActivityOrNull)

internal object ContextManager {

    var topActivity: WeakReference<Activity>? = null

    var application: Application? = null

    fun init(application: Application) {
        this.application = application
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
        })
    }


}



