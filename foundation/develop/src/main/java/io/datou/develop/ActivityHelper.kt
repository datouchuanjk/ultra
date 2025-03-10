package io.datou.develop

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.concurrent.CopyOnWriteArrayList

val TopActivity get() = ActivityHelper.topActivity

internal object ActivityHelper {

    private val _activities = CopyOnWriteArrayList<WeakReference<Activity>>()

    private val activities: List<Activity>
        get() = _activities.mapNotNull { it.get() }

    internal val topActivity
        get() = activities.lastOrNull()

    internal fun finishActivities() {
        activities.forEach { it.finish() }
    }

    internal fun registerActivityLifecycleCallbacks(application: Application) {
        application.registerActivityLifecycleCallbacks(object :
            ActivityLifecycleCallbacks by noOpDelegate() {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                _activities.add(WeakReference(activity))
            }

            override fun onActivityDestroyed(activity: Activity) {
                _activities.removeIf {
                    it.get() === activity
                }
            }
        }
        )
    }
}