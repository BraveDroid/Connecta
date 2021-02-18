package com.bravedroid.connecta.infrastructure

import android.content.Context
import android.net.ConnectivityManager
import com.bravedroid.connecta.api.ConnectionManagerFlow
import kotlin.coroutines.CoroutineContext

object ConnectionManagerProvider {
    fun createConnectionManagerFlow(
        context: Context,
        coroutineContext: CoroutineContext
    ): ConnectionManagerFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ConnectionManagerFlow(connectivityManager, coroutineContext)
    }
}
