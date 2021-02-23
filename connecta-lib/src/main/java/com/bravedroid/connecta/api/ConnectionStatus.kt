package com.bravedroid.connecta.api

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

enum class ConnectionStatus {
    UNKNOWN,
    CONNECTED,
    NOT_CONNECTED;
}

sealed class InternetConnectionStatus {
    object Unknown

    //connected to internet
    sealed class Connected : InternetConnectionStatus() {
        object WithWifi : Connected()
        object WithCellular : Connected()
        object Other : Connected()
    }

    //no internet
    sealed class NotConnected : InternetConnectionStatus() {
        //no cellular no wifi no AirPlane mode,
        object NoSignal : NotConnected()

        //connected to a cellular or a wifi, no AirPlane mode
        object NoInternet : NotConnected()

        //no cellular no wifi no AirPlane mode but AirPlane mode
        object AirPlaneMode : NotConnected()
    }
}

fun findConnectedStatusWhenHasNetwork(
    connectedToInternet: Boolean,
    cm: ConnectivityManager,
    network: Network
): InternetConnectionStatus {
    val networkCapabilities = cm.getNetworkCapabilities(network)
    return when {
        connectedToInternet && networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> InternetConnectionStatus.Connected.WithWifi
        connectedToInternet && networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> InternetConnectionStatus.Connected.WithCellular
        connectedToInternet -> InternetConnectionStatus.Connected.Other
        !connectedToInternet -> InternetConnectionStatus.NotConnected.NoInternet
        else -> throw IllegalStateException()
    }
}
