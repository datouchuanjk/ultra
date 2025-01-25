package io.standard.tools

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private val InternalNetworkObserver = MutableStateFlow(
    App.getSystemService<ConnectivityManager>().getNetworkStatus()
)

val NetworkObserver = InternalNetworkObserver.asStateFlow()

@SuppressLint("MissingPermission")
internal fun registerNetworkObserver() {
    val connectivityManager = App.getSystemService<ConnectivityManager>()
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            InternalNetworkObserver.value = connectivityManager.getNetworkStatus()
        }

        override fun onLost(network: Network) {
            InternalNetworkObserver.value = NetworkStatus.Unknown
        }
    }
    connectivityManager?.registerDefaultNetworkCallback(callback)
}

@SuppressLint("MissingPermission")
private fun ConnectivityManager?.getNetworkStatus(network: Network? = this?.activeNetwork) =
    this?.getNetworkCapabilities(network)?.let {
        when {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkStatus.Wifi
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                NetworkStatus.Mobile(it.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED))
            }

            else -> NetworkStatus.Other
        }
    } ?: NetworkStatus.Unknown


sealed class NetworkStatus {
    data object Unknown : NetworkStatus()
    data object Wifi : NetworkStatus()
    data class Mobile(val isUnlimited: Boolean) : NetworkStatus()
    data object Other : NetworkStatus()
}