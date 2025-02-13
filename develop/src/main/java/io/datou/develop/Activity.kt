package io.datou.develop

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference

internal val InternalActivities = LinkedHashMap<Int, WeakReference<Activity>>()

val Activities
    get() = InternalActivities
        .map { it.value }
        .mapNotNull { it.get() }

val PeekActivity
    get() = Activities.lastOrNull()

fun finishActivities() {
    Activities.forEach {
        it.finish()
    }
}

internal fun registerActivitiesObserver() {
    App.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks by noOpDelegate() {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            InternalActivities[activity.hashCode()] = WeakReference(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
            InternalActivities.remove(activity.hashCode())
        }
    }
    )
}