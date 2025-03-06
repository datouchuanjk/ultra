package io.datou.develop

import android.Manifest
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


val CurrentNetworkStateFlow get() = NetworkHelper().currentStateFlow

class NetworkHelper {

    val currentStateFlow: StateFlow<State>
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE) get() {
            val mutableStateFlow = MutableStateFlow(defaultCurrentState)
            addObserver {
                mutableStateFlow.value = it.currentState
            }
            return mutableStateFlow.asStateFlow()
        }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    internal fun addObserver(block: (Network) -> Unit) {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                block(network)
            }

            override fun onLost(network: Network) {
                block(network)
            }
        }
        App.getSystemService<ConnectivityManager>()?.registerDefaultNetworkCallback(callback)
    }

    private val defaultCurrentState
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        get() = App.getSystemService<ConnectivityManager>()
            ?.activeNetwork?.currentState
            ?: State.Unknown(false)

    private val Network.currentState: State
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE) get() {
            val manager = App.getSystemService<ConnectivityManager>()
            return manager?.getNetworkCapabilities(this)
                ?.let {
                    val isVpn = it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    when {
                        it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> State.Wifi(
                            isVpn = isVpn
                        )

                        it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            State.Mobile(
                                isUnlimited = it.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED),
                                isVpn = isVpn
                            )
                        }

                        else -> State.Other(isVpn = isVpn)
                    }
                } ?: State.Unknown(false)
        }

    sealed class State(val isVpn: Boolean) {
        class Unknown(isVpn: Boolean) : State(isVpn)
        class Wifi(isVpn: Boolean) : State(isVpn)
        class Mobile(isUnlimited: Boolean, isVpn: Boolean) : State(isVpn)
        class Other(isVpn: Boolean) : State(isVpn)
    }
}