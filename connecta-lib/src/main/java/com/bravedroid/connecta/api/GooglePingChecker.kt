package com.bravedroid.connecta.api

import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

internal object GooglePingChecker {

    internal fun hasTcpConnection(socket: Socket? = null): Boolean =
        try {
            (socket ?: Socket()).let {
                Timber.d("PINGING google.")
                it.connect(InetSocketAddress("8.8.8.8", 53), 1_500)
                Timber.d("PING success.")
                it.close()
            }
            true
        } catch (e: IOException) {
            Timber.e("No internet connection. $e")
            false
        }
}
