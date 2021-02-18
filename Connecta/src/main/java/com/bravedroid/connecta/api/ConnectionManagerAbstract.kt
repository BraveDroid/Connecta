package com.bravedroid.connecta.api

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bravedroid.connecta.api.GooglePingChecker.hasTcpConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

// All this work should not be in Main thread

abstract class ConnectionManagerAbstract(
    private val cm: ConnectivityManager,
    final override val coroutineContext: CoroutineContext,
) : CoroutineScope {
    private val validNetworks: MutableSet<Network> = HashSet()
    private val hasValidNetwork get() = validNetworks.isNotEmpty()
    private val networkCallback: ConnectivityManager.NetworkCallback = CheckerNetworkCallback(
        cm = cm,
        onNetworkAvailableAction = {
            validNetworks += it
            if (hasValidNetwork) {
                updateStream(ConnectionState.CONNECTED)
            }
        },
        onNetworkLostAction = {
            validNetworks -= it
            if (!hasValidNetwork) {
                updateStream(ConnectionState.NOT_CONNECTED)
            }
        },
        coroutineContext,
    )

    // should be called to start looking for network
    fun onStart() {
        launch {
            val hasTcpConnection = withContext(coroutineContext) {
                hasTcpConnection()
            }

            updateStream(if (hasTcpConnection) ConnectionState.CONNECTED else ConnectionState.NOT_CONNECTED)
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET)
                .build()
            cm.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    // should be called to stop looking for network and release callback
    fun onStop() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    protected abstract fun updateStream(connectionState: ConnectionState)
}

class ConnectionManagerFlow(
    cm: ConnectivityManager,
    coroutineContext: CoroutineContext,
) : ConnectionManagerAbstract(cm, coroutineContext) {
    private val _isConnectedToInternet = MutableStateFlow<ConnectionState>(ConnectionState.UNKNOWN)
    val isConnectedToInternet: Flow<ConnectionState> = _isConnectedToInternet
    override fun updateStream(connectionState: ConnectionState) {
        _isConnectedToInternet.value = connectionState
    }
}

class ConnectionManagerLiveData(
    cm: ConnectivityManager,
    coroutineContext: CoroutineContext,
) : ConnectionManagerAbstract(cm, coroutineContext) {
    private val _isConnectedToInternet = MutableLiveData<ConnectionState>(ConnectionState.UNKNOWN)
    val isConnectedToInternet: LiveData<ConnectionState> = _isConnectedToInternet
    override fun updateStream(connectionState: ConnectionState) {
        _isConnectedToInternet.postValue(connectionState)
    }
}
