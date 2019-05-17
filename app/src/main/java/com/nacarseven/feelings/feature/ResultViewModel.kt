package com.nacarseven.feelings.feature

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nacarseven.feelings.repository.ResultRepositoryContract
import com.nacarseven.feelings.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class ResultViewModel(
    private var resultRepository: ResultRepositoryContract
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val stream = PublishSubject.create<Intention>()

    private val _state = MutableLiveData<ScreenState>()
    val state: LiveData<ScreenState>
        get() = _state

    private val _events = MutableLiveData<Event<SideEffect>>()
    val events: LiveData<Event<SideEffect>>
        get() = _events


    sealed class Intention {
        data class SearchTweets(val text: String) : Intention()
        object ShowErrorMessage : Intention()
    }

    sealed class ScreenState {
        object Empty : ScreenState()
        object Loading : ScreenState()
        data class Error(val message: CharSequence) : ScreenState()
        data class Result(val hasResultToShow: Boolean) : ScreenState()
    }

    sealed class SideEffect {
        object ClearFieldSearch : SideEffect()
    }

}