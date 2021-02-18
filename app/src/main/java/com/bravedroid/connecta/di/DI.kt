package com.bravedroid.connecta.di

import android.content.Context
import com.bravedroid.connecta.api.ConnectionManagerFlow
import com.bravedroid.connecta.app.ConnectaDemoApplication
import com.bravedroid.connecta.domain.events.EventBus
import com.bravedroid.connecta.domain.events.EventBusImpl
import com.bravedroid.connecta.domain.system.SystemConnectionManagerFlow
import com.bravedroid.connecta.infrastructure.system.SystemConnectionManagerImpl

object DI {
    private fun injectApplication(context: Context): ConnectaDemoApplication =
        context.applicationContext as ConnectaDemoApplication

    private fun injectConnectionManagerFlow(application: ConnectaDemoApplication) =
        application.systemModule.connectionManagerFlow

    private fun injectConnectionManagerFlow(context: Context): ConnectionManagerFlow {
        val injectApplication = injectApplication(context)
        return injectConnectionManagerFlow(injectApplication)
    }

    fun injectSystemConnectionManagerFlow(context: Context): SystemConnectionManagerFlow {
        val cm = injectConnectionManagerFlow(context)
        return SystemConnectionManagerImpl(cm)
    }

    fun injectEventBus(): EventBus = EventBusImpl
}

