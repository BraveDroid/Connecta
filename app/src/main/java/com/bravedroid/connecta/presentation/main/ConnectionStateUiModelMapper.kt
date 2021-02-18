package com.bravedroid.connecta.presentation.main

import com.bravedroid.connecta.domain.enteties.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.atomic.AtomicBoolean

class ConnectionStateUiModelMapper(
    private var hasReceivedNotConnected: AtomicBoolean,
    private val hasReceivedNotConnectedMutableStateFlow: MutableStateFlow<Boolean>,

    ) {
    fun mapToConnectionStateUiModel(connectionState: ConnectionState): ConnectionStateUiModel? =
        when {
            connectionState == ConnectionState.CONNECTED && hasReceivedNotConnected.get() -> {
                ConnectionStateUiModel.DISPLAY_CONNECTED_STATE
            }
            connectionState == ConnectionState.CONNECTED && !hasReceivedNotConnected.get() -> {
                null
            }
            else -> {
                hasReceivedNotConnected.set(true)
                ConnectionStateUiModel.DISPLAY_NOT_CONNECTED_STATE
            }
        }

    fun mapToConnectionStateUiModel1(
        connectionState: ConnectionState,
    ): Flow<ConnectionStateUiModel> =
        if (connectionState == ConnectionState.NOT_CONNECTED) {
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
