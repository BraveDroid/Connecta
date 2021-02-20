package com.bravedroid.connecta.infrastructure.system

import com.bravedroid.connecta.api.ConnectionManagerFlow
import com.bravedroid.connecta.domain.system.SystemConnectionManagerFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map

class SystemConnectionManagerImpl(private val cm: ConnectionManagerFlow) :
    SystemConnectionManagerFlow {
    override fun checkConnectionAsFlow() = cm.isConnectedToInternet.filterNot {
        it == com.bravedroid.connecta.api.ConnectionStatus.UNKNOWN
    }.map(ConnectionStateMapper::mapToConnectionStatus)

    override fun startChecking() = cm.startCheckingNetworkStatus()
    override fun stopChecking() = cm.stopCheckingNetworkStatus()
}
