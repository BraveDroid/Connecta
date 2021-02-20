package com.bravedroid.connecta.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bravedroid.connecta.domain.entities.ConnectionStatus
import com.bravedroid.connecta.domain.events.EventBus
import com.bravedroid.connecta.domain.usecases.CheckNetworkStateUsesCases
import com.bravedroid.connecta.presentation.events.ShowConnectedStateEvent
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

class MainViewModel(
    private val checkNetworkStateUsesCases: CheckNetworkStateUsesCases,
    private val eventBus: EventBus,
) : ViewModel() {
    init {
        Timber.d(" MainViewModel hashCode ${hashCode()}")
        checkNetworkStateUsesCases.startChecking()
    }

    private var hasReceivedNotConnected: AtomicBoolean = AtomicBoolean(false)

    private var hasReceivedNotConnectedMutableStateFlow: MutableStateFlow<Boolean> =
        MutableStateFlow<Boolean>(false)
    private val connectionStateUiModelMapper = ConnectionStateUiModelMapper(
        hasReceivedNotConnected,
        hasReceivedNotConnectedMutableStateFlow,
    )


    init {
        checkNetworkStateUsesCases.checkConnection()
            .onEach {
                Timber.tag("EVENTBUS").d(" init1 $it")
            }
            .filter {
                it == ConnectionStatus.CONNECTED && hasReceivedNotConnected.get()
            }
            .onEach {
                Timber.tag("EVENTBUS").d(" init2 $it")
            }
            .onEach {
                hasReceivedNotConnected.set(true)
                eventBus.sendEvent(ShowConnectedStateEvent)
            }
            .launchIn(viewModelScope)
    }


    val hasAvailableConnection: LiveData<ConnectionStateUiModel> =
        checkNetworkStateUsesCases.checkConnection()
            .onEach { Timber.d("onEach $it") }
            .map(connectionStateUiModelMapper::mapToConnectionStateUiModel)
            .filterNotNull()
            .asLiveData()

    val hasAvailableConnection2 =
        checkNetworkStateUsesCases.checkConnection()
            .onEach { Timber.d("onEach $it") }
            .map(connectionStateUiModelMapper::mapToConnectionStateUiModel)
            .filterNotNull()

    val hasAvailableConnection5 =
        checkNetworkStateUsesCases.checkConnection()
            .onEach { Timber.d("onEach $it") }
            .flatMapLatest(connectionStateUiModelMapper::mapToConnectionStateUiModel1)
            .asLiveData()

    override fun onCleared() {
        checkNetworkStateUsesCases.stopChecking()
    }
}

