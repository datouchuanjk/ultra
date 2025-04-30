package io.datou.develop

import android.Manifest
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

val CurrentActiveNetworkFlow: ( CoroutineScope) -> StateFlow<Network?>
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    get() = { scope ->
        val connectivityManager = AppContext.getSystemService<ConnectivityManager>()
        val state = MutableStateFlow(connectivityManager?.activeNetwork)
        scope.launch(Dispatchers.Main) {
            callbackFlow {
                val callback = object : ConnectivityManager.NetworkCallback() {
                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities
                    ) {
                        super.onCapabilitiesChanged(network, networkCapabilities)
                        trySend(network)
                    }
                }
                connectivityManager?.registerDefaultNetworkCallback(callback)
                awaitClose {
                    connectivityManager?.unregisterNetworkCallback(callback)
                }
            }.collect {
                state.value = it
            }
        }
        state.asStateFlow()
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
