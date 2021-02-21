package com.bravedroid.connecta.presentation.main

import com.bravedroid.connecta.domain.entities.ConnectionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.atomic.AtomicBoolean

class ConnectionStateUiModelMapper(
    private var hasReceivedNotConnected: AtomicBoolean,
    private val hasReceivedNotConnectedMutableStateFlow: MutableStateFlow<Boolean>,

    ) {
    fun mapToConnectionStateUiModel(connectionStatus: ConnectionStatus): ConnectionStateUiModel? =
        when {
            connectionStatus == ConnectionStatus.CONNECTED && hasReceivedNotConnected.get() -> {
                ConnectionStateUiModel.DISPLAY_CONNECTED_STATE
            }
            connectionStatus == ConnectionStatus.CONNECTED && !hasReceivedNotConnected.get() -> {
                null
            }
            else -> {
                hasReceivedNotConnected.set(true)
                ConnectionStateUiModel.DISPLAY_NOT_CONNECTED_STATE
            }
        }

    fun mapToConnectionStateUiModel1(
        connectionStatus: ConnectionStatus,
    ): Flow<ConnectionStateUiModel> =
        if (connectionStatus == ConnectionStatus.NOT_CONNECTED) {
            hasReceivedNotConnectedMutableStateFlow.value = true
            ConnectionStateUiModel.DISPLAY_NOT_CONNECTED_STATE.toFlow()
        } else {
            val hasReceivedNotConnected = hasReceivedNotConnectedMutableStateFlow.value
            if (hasReceivedNotConnected)
                ConnectionStateUiModel.DISPLAY_CONNECTED_STATE.toFlow()
            else
                flowOf()
        }

    private fun <T> T.toFlow(): Flow<T> = flowOf(this)

}
