package io.datou.develop

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

val Instance: Application by lazy {
    checkNotNull(ContextManager.application)
}

val TopActivity get() = ContextManager.topActivity?.get()

fun Application.startDevelop() {
    ContextManager.init(this)
}

internal object ContextManager {

    var topActivity: WeakReference<Activity>? = null
    var application: Application? = null

    fun init(application: Application) {
        this.application = application
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks by noOpDelegate() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                topActivity = WeakReference(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                if (activity == topActivity?.get()) {
                    topActivity = null
                }
            }
        })
    }
}



