package com.bravedroid.connecta.api

enum class ConnectionState {
    UNKNOWN,
    CONNECTED,
    NOT_CONNECTED;
}

private sealed class _ConnectionState {
    object Unknown
    sealed class Connected {
        class WithWifi
        class WithCellular
    }

    sealed class NotConnected {
        class NoSignal
        class NoInternet
    }
}
