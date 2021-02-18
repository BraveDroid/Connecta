package com.bravedroid.connecta.domain.system

import androidx.lifecycle.LiveData
import com.bravedroid.connecta.domain.enteties.ConnectionState
import kotlinx.coroutines.flow.Flow

interface SystemConnectionManager {
    fun startChecking()
    fun stopChecking()
}

interface SystemConnectionManagerLiveData : SystemConnectionManager {
    fun checkConnectionAsLiveData(): LiveData<ConnectionState>
}

interface SystemConnectionManagerFlow : SystemConnectionManager {
    fun checkConnectionAsFlow(): Flow<ConnectionState>
}
