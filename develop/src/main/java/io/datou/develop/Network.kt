package io.datou.develop

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleOwner

internal val Context.connectivityManager get() = getSystemService<ConnectivityManager>()

val Context.activeNetwork
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = connectivityManager?.activeNetwork

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun LifecycleOwner.registerDefaultNetworkCallback(block: (NetworkCapabilities) -> Unit) {
    withLifecycleDestroyed {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                block(networkCapabilities)
            }
        }
        AppContext.connectivityManager?.registerDefaultNetworkCallback(callback)
        onDestroyed {
            AppContext.connectivityManager?.unregisterNetworkCallback(callback)
        }
    }
}

val Network.isConnected: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.getSystemService<ConnectivityManager>()
        ?.getNetworkCapabilities(this)
        ?.isConnected == true

val Network.isWifi: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.connectivityManager
        ?.getNetworkCapabilities(this)
        ?.isWifi == true

val Network.isCellular: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.connectivityManager
        ?.getNetworkCapabilities(this)
        ?.isCellular == true

val Network.isVpn: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.connectivityManager
        ?.getNetworkCapabilities(this)
        ?.isVpn == true

val Network.isEthernet: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.connectivityManager
        ?.getNetworkCapabilities(this)
        ?.isEthernet == true

val Network.isBluetooth: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.connectivityManager
        ?.getNetworkCapabilities(this)
        ?.isBluetooth == true


val NetworkCapabilities.isConnected: Boolean
    get() = hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

val NetworkCapabilities.isWifi: Boolean
    get() = hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

val NetworkCapabilities.isCellular: Boolean
    get() = hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

val NetworkCapabilities.isVpn: Boolean
    get() = hasTransport(NetworkCapabilities.TRANSPORT_VPN)

val NetworkCapabilities.isEthernet: Boolean
    get() = hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

val NetworkCapabilities.isBluetooth: Boolean
    get() = hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
