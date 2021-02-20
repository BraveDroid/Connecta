package com.bravedroid.connecta.domain.system

import androidx.lifecycle.LiveData
import com.bravedroid.connecta.domain.entities.ConnectionStatus
import kotlinx.coroutines.flow.Flow

interface SystemConnectionManager {
    fun startChecking()
    fun stopChecking()
}

interface SystemConnectionManagerLiveData : SystemConnectionManager {
    fun checkConnectionAsLiveData(): LiveData<ConnectionStatus>
}

interface SystemConnectionManagerFlow : SystemConnectionManager {
    fun checkConnectionAsFlow(): Flow<ConnectionStatus>
}
