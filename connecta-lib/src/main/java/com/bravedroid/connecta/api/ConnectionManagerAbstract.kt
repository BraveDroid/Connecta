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
    private fun updateStream() {
        if (hasValidNetwork) {
            updateStream(ConnectionStatus.CONNECTED)
        } else {
            updateStream(ConnectionStatus.NOT_CONNECTED)
        }
    }

    private val networkCallback: ConnectivityManager.NetworkCallback = CheckerNetworkCallback(
        cm = cm,
        onNetworkAvailableAction = {
            validNetworks += it
            updateStream()
        },
        onNetworkLostAction = {
            validNetworks -= it
            updateStream()
        },
        coroutineContext,
    )

    // should be called to start looking for network
    fun startCheckingNetworkStatus() {
        launch {
            val hasTcpConnection = withContext(coroutineContext) {
                hasTcpConnection()
            }

            updateStream(if (hasTcpConnection) ConnectionStatus.CONNECTED else ConnectionStatus.NOT_CONNECTED)
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NET_CAPABILITY_INTERNET)
                .build()
            cm.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    // should be called to stop looking for network and release callback
    fun stopCheckingNetworkStatus() {
        cm.unregisterNetworkCallback(networkCallback)
    }

    protected abstract fun updateStream(connectionStatus: ConnectionStatus)
}

class ConnectionManagerFlow(
    cm: ConnectivityManager,
    coroutineContext: CoroutineContext,
) : ConnectionManagerAbstract(cm, coroutineContext) {
    private val _isConnectedToInternet =
        MutableStateFlow<ConnectionStatus>(ConnectionStatus.UNKNOWN)
    val isConnectedToInternet: Flow<ConnectionStatus> = _isConnectedToInternet
    override fun updateStream(connectionStatus: ConnectionStatus) {
        _isConnectedToInternet.value = connectionStatus
    }
}

class ConnectionManagerLiveData(
    cm: ConnectivityManager,
    coroutineContext: CoroutineContext,
) : ConnectionManagerAbstract(cm, coroutineContext) {
    private val _isConnectedToInternet = MutableLiveData<ConnectionStatus>(ConnectionStatus.UNKNOWN)
    val isConnectedToInternet: LiveData<ConnectionStatus> = _isConnectedToInternet
    override fun updateStream(connectionStatus: ConnectionStatus) {
        _isConnectedToInternet.postValue(connectionStatus)
    }
}
