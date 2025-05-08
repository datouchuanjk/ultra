package io.datou.develop

import android.Manifest
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

val LifecycleOwner.currentNetworkFlow: StateFlow<Network?>
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() {
        val connectivityManager = AppContext.getSystemService<ConnectivityManager>()
        val state = MutableStateFlow(connectivityManager?.activeNetwork)
        withLifecycleDestroyed {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    state.value = network
                }
            }
            connectivityManager?.registerDefaultNetworkCallback(callback)
            onDestroyed {
                connectivityManager?.unregisterNetworkCallback(callback)
            }
        }
        return state.asStateFlow()
    }

val Network.isConnected: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.getSystemService<ConnectivityManager>()
        ?.getNetworkCapabilities(this)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

val Network.isWifi: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.getSystemService<ConnectivityManager>()
        ?.getNetworkCapabilities(this)
        ?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

val Network.isCellular: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.getSystemService<ConnectivityManager>()
        ?.getNetworkCapabilities(this)
        ?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true

val Network.isVpn: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.getSystemService<ConnectivityManager>()
        ?.getNetworkCapabilities(this)
        ?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true

val Network.isEthernet: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.getSystemService<ConnectivityManager>()
        ?.getNetworkCapabilities(this)
        ?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true

val Network.isBluetooth: Boolean
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = AppContext.getSystemService<ConnectivityManager>()
        ?.getNetworkCapabilities(this)
        ?.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) == true
