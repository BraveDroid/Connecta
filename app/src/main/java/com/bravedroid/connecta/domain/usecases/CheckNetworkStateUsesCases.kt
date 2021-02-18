package com.bravedroid.connecta.domain.usecases

import com.bravedroid.connecta.domain.system.SystemConnectionManagerFlow

class CheckNetworkStateUsesCases(
    private val scm: SystemConnectionManagerFlow,
) {
    fun startChecking() = scm.startChecking()
    fun checkConnection() = scm.checkConnectionAsFlow()
    fun stopChecking() = scm.stopChecking()
}
