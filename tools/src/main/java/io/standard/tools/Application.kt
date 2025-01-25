package io.standard.tools

import android.Manifest
import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope

internal var InternalApp: Application? = null
    set(value) {
        if (field == null && value != null) {
            field = value
            registerProcessObserver()
            if (isPermissionGranted(Manifest.permission.ACCESS_NETWORK_STATE)) {
                registerNetworkObserver()
            }
            registerActivitiesObserver()
        }
    }

val App get() = checkNotNull(InternalApp)

val AppScope get() = ProcessLifecycleOwner.get().lifecycleScope





