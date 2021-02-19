package com.bravedroid.connecta.api

import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object GooglePingChecker {

    fun hasTcpConnection(): Boolean =
        try {
            Timber.d("PINGING google.")
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1_500)
            socket.close()
            Timber.d("PING success.")
            true
        } catch (e: IOException) {
            Timber.e("No internet connection. $e")
            false
        }
}
