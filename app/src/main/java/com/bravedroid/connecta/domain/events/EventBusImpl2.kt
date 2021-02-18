package com.bravedroid.connecta.domain.events

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

//class EventBusImpl(
//    override val coroutineContext: CoroutineContext,
//) : EventBus, CoroutineScope {

object EventBusImpl2 : EventBus {
    private val eventStream = Channel<EventBus.Event>()
    override val oneShotEventStream: Flow<EventBus.Event> = eventStream.receiveAsFlow()
    override fun sendEvent(event: EventBus.Event) = eventStream.offer(event)
    suspend fun sendEventEventé(event: EventBus.Event) = eventStream.send(event)
//    fun sendEventEvent1(event: EventBus.Event) = launch {
//        eventStream.emit(event)
//    }


}
