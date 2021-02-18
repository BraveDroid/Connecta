package com.bravedroid.connecta.infrastructure.system

import com.bravedroid.connecta.domain.enteties.ConnectionState
import com.bravedroid.connecta.api.ConnectionState as Connecta

object ConnectionStateMapper {
    fun mapToConnectionState(
        connectionState: com.bravedroid.connecta.api.ConnectionState
    ): ConnectionState =
        if (connectionState == Connecta.CONNECTED) {
            ConnectionState.CONNECTED
        } else {
            ConnectionState.NOT_CONNECTED
        }
}
