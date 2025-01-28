package io.datou.develop

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference

internal val WeakReferenceActivities = LinkedHashMap<Int, WeakReference<Activity>>()

val Activities
    get() = WeakReferenceActivities
        .map { it.value }
        .mapNotNull { it.get() }

val PeekActivity
    get() = Activities.lastOrNull()

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