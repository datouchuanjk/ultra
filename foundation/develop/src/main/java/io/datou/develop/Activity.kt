package io.datou.develop

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import java.lang.ref.WeakReference

internal val InternalActivities = LinkedHashMap<Int, WeakReference<Activity>>()

val  ComponentActivities: List<ComponentActivity>
    get() = InternalActivities
        .map { it.value }
        .map { it.get() }
        .filterIsInstance<ComponentActivity>()

val CurrentResumeActivity: ComponentActivity?
    get() = ComponentActivities
        .reversed()
        .find {
            it.lifecycle.currentState == Lifecycle.State.RESUMED
        }

val StackTopActivity: ComponentActivity?
    get() = ComponentActivities.lastOrNull()

fun finishActivities() {
    InternalActivities.forEach {
        it.value.get()?.finish()
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