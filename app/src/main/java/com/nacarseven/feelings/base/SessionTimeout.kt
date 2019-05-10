package com.nacarseven.feelings.base

import com.nacarseven.feelings.util.Event
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

class SessionTimeout {

    // so if onNext gets called when no one is subscribed it re-emits the event
    private val stream = BehaviorSubject.create<Event<Unit>>()

    fun observe(block: (Unit) -> Unit): Disposable = stream
        .filter { it.getContentIfNotHandled() != null }
        .map { Unit }
        .subscribe(block)

    fun signal() {
        stream.onNext(Event(Unit))
    }
}