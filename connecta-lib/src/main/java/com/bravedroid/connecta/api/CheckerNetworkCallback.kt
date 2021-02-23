package com.bravedroid.connecta.api

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

internal class CheckerNetworkCallback(
    private val cm: ConnectivityManager,
    private val onNetworkAvailableAction: (network: Network) -> Unit,
    private val onNetworkLostAction: (network: Network) -> Unit,
    override val coroutineContext: CoroutineContext,
) : ConnectivityManager.NetworkCallback(), CoroutineScope {

    override fun onAvailable(network: Network) {
        val hasInternetCapability = cm.getNetworkCapabilities(network)
            ?.hasCapability(NET_CAPABILITY_INTERNET) ?: false

        Timber.d("onAvailable: $network, $hasInternetCapability")

        if (hasInternetCapability) {
            launch {
                val socket = network.socketFactory.createSocket()
                val hasTcpConnection = GooglePingChecker.hasTcpConnection(socket)
                if (hasTcpConnection) {
                    Timber.d("onAvailable: adding network. $network")
                    onNetworkAvailableAction(network)
                } else {
                    onNetworkLostAction(network)
                }
            }
        }
    }

    override fun onLost(network: Network) {
        Timber.d("onLost: $network")
        onNetworkLostAction(network)
    }
}
