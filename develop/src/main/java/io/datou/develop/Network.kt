package io.datou.develop

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal val InternalNetworkObserver = MutableStateFlow(networkStatus)

val NetworkObserver = InternalNetworkObserver.asStateFlow()

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
internal fun registerNetworkObserver() {
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            InternalNetworkObserver.value = networkStatus
        }

        override fun onLost(network: Network) {
            InternalNetworkObserver.value = NetworkStatus.Unknown
        }
    }
    App.getSystemService<ConnectivityManager>()?.registerDefaultNetworkCallback(callback)
}

internal val networkStatus: NetworkStatus
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() {
        val manager = App.getSystemService<ConnectivityManager>()
        return manager?.getNetworkCapabilities(manager.activeNetwork)
            ?.let {
                when {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkStatus.Wifi
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        NetworkStatus.Mobile(it.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED))
                    }

                    else -> NetworkStatus.Other
                }
            } ?: NetworkStatus.Unknown
    }


sealed class NetworkStatus {
    data object Unknown : NetworkStatus()
    data object Wifi : NetworkStatus()
    data class Mobile(val isUnlimited: Boolean) : NetworkStatus()
    data object Other : NetworkStatus()
}