package io.datou.develop

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import io.datou.develop.CustomActivityResultContracts.OpenPermissionSettings.Companion.isPermissionGranted

internal var InternalApp: Application? = null
    @SuppressLint("MissingPermission")
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

val AppLifecycleOwner get() = ProcessLifecycleOwner.get()






