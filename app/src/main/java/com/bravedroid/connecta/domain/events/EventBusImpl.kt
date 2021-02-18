package com.bravedroid.connecta.domain.events

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onCompletion

//class EventBusImpl(
//    override val coroutineContext: CoroutineContext,
//) : EventBus, CoroutineScope {

object EventBusImpl : EventBus {
    private val eventStream = MutableSharedFlow<EventBus.Event>(1)
    override val oneShotEventStream: Flow<EventBus.Event> = eventStream.onCompletion {
        reset()
    }

    override fun sendEvent(event: EventBus.Event) = eventStream.tryEmit(event)
//    fun sendEventEvent1(event: EventBus.Event) = launch {
//        eventStream.emit(event)
//    }

    private fun reset() {
        eventStream.resetReplayCache()
    }
}
