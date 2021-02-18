package com.bravedroid.connecta.domain.events

import kotlinx.coroutines.flow.Flow

interface EventBus {
    sealed class Event {
        abstract class UiEvent : Event()
        abstract class DataEvent : Event()
        sealed class CmpEvent : Event() {
            object FireBaseIsReadyEvent
            object RoomIsReadyEvent
            object TrackingIsReadyEvent
        }
    }

    val oneShotEventStream: Flow<Event>
    fun sendEvent(event: Event): Boolean
}

