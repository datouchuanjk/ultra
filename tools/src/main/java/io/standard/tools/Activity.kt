package io.standard.tools

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference

internal val WeakReferenceActivities = LinkedHashMap<Int, WeakReference<Activity>>()

val Activities
    get() = WeakReferenceActivities
        .map { it.value }
        .map { it.get() }

val PeekActivity
    get() = Activities.lastOrNull()

fun killAllActivities() {
    Activities
        .mapNotNull {
            it
        }.forEach {
            it.finish()
        }
}

internal fun registerActivitiesObserver() {
    App.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks by noOpDelegate() {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            WeakReferenceActivities[activity.hashCode()] = WeakReference(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            WeakReferenceActivities.remove(activity.hashCode())
        }
    }
    )
}