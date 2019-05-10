package com.nacarseven.feelings.util

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ConnectivityErrorObservable {

    private val stream = PublishSubject.create<Unit>()

    fun observe(): Observable<Unit> = stream.hide()

    fun sendEvent() {
        stream.onNext(Unit)
    }
}