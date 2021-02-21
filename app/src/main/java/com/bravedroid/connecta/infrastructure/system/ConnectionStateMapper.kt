package com.bravedroid.connecta.infrastructure.system

import com.bravedroid.connecta.domain.entities.ConnectionStatus
import com.bravedroid.connecta.api.ConnectionStatus as Connecta

object ConnectionStateMapper {
    fun mapToConnectionStatus(
        connectionStatus: com.bravedroid.connecta.api.ConnectionStatus
    ): ConnectionStatus =
        if (connectionStatus == Connecta.CONNECTED) {
            ConnectionStatus.CONNECTED
        } else {
            ConnectionStatus.NOT_CONNECTED
        }
}
