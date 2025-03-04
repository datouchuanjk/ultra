package io.datou.develop

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference

object ActivityHelper {

    private val _weakReferenceActivities = mutableListOf<WeakReference<Activity>>()

    private val _activities: List<Activity>
        get() = _weakReferenceActivities
            .mapNotNull { it.get() }

    val topActivity: Activity
        get() = _activities.last()

    fun finishActivities() {
        _activities.forEach {
            it.finish()
        }
    }

    internal fun registerActivityLifecycleCallbacks() {
        App.registerActivityLifecycleCallbacks(object :
            ActivityLifecycleCallbacks by noOpDelegate() {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                _weakReferenceActivities.add(WeakReference(activity))
            }

            override fun onActivityDestroyed(activity: Activity) {
                _weakReferenceActivities.removeIf {
                    it.get() == activity
                }
            }
        }
        )
    }
}