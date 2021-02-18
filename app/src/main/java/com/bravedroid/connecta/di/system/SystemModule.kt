package com.bravedroid.connecta.di.system

import android.content.Context
import com.bravedroid.connecta.api.ConnectionManagerFlow
import com.bravedroid.connecta.infrastructure.ConnectionManagerProvider
import kotlin.coroutines.CoroutineContext

class SystemModule(
    context: Context,
    coroutineContext: CoroutineContext
) {
    val connectionManagerFlow: ConnectionManagerFlow by lazy {
        ConnectionManagerProvider.createConnectionManagerFlow(context, coroutineContext)
    }
}
