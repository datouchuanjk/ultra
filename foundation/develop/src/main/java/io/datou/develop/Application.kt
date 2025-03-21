package io.datou.develop

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

private var GLOBAL_APPLICATION: Application? = null
private var TOP_ACTIVITY: WeakReference<Activity>? = null

val InstanceApp: Application get() = checkNotNull(GLOBAL_APPLICATION)
val TopActivity get() = TOP_ACTIVITY?.get()

fun Application.develop() {
    GLOBAL_APPLICATION = this
    registerActivityLifecycleCallbacks(object :
        Application.ActivityLifecycleCallbacks by noOpDelegate() {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            TOP_ACTIVITY = WeakReference(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activity == TOP_ACTIVITY?.get()) {
                TOP_ACTIVITY = null
            }
        }
    })
}



