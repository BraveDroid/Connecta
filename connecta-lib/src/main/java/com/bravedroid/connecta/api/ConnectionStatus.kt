package com.bravedroid.connecta.api

enum class ConnectionStatus {
    UNKNOWN,
    CONNECTED,
    NOT_CONNECTED;
}

private sealed class _ConnectionStatus {
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
