package io.datou.develop

import android.Manifest
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal val InternalNetworkObserver = MutableStateFlow(CurrentNetworkStatus)

val NetworkObserver = InternalNetworkObserver.asStateFlow()

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
internal fun registerNetworkObserver() {
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            InternalNetworkObserver.value = CurrentNetworkStatus
        }

        override fun onLost(network: Network) {
            InternalNetworkObserver.value = NetworkStatus.Unknown(false)
        }
    }
    App.getSystemService<ConnectivityManager>()?.registerDefaultNetworkCallback(callback)
}

internal val CurrentNetworkStatus: NetworkStatus
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() {
        val manager = App.getSystemService<ConnectivityManager>()
        return manager?.getNetworkCapabilities(manager.activeNetwork)
            ?.let {
                val isVpn = it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                when {
                    it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkStatus.Wifi(isVpn = isVpn)
                    it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        NetworkStatus.Mobile(
                            isUnlimited = it.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED),
                            isVpn = isVpn
                        )
                    }

                    else -> NetworkStatus.Other(isVpn = isVpn)
                }
            } ?: NetworkStatus.Unknown(false)
    }


sealed class NetworkStatus(val isVpn: Boolean) {
    class Unknown(isVpn: Boolean) : NetworkStatus(isVpn)
    class Wifi(isVpn: Boolean) : NetworkStatus(isVpn)
    class Mobile(isUnlimited: Boolean, isVpn: Boolean) : NetworkStatus(isVpn)
    class Other(isVpn: Boolean) : NetworkStatus(isVpn)
}