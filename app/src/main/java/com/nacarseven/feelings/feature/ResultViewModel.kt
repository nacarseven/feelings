package com.nacarseven.feelings.feature

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import com.nacarseven.feelings.repository.ResultRepositoryContract
import com.nacarseven.feelings.util.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class ResultViewModel(
    private var resultRepository: ResultRepositoryContract
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val stream = PublishSubject.create<Intention>()

    private val _state = MutableLiveData<Event<ScreenState>>()
    val state: LiveData<Event<ScreenState>>
        get() = _state


    init {

        val result: Observable<ScreenState> = stream
            .ofType(Intention.GetResultCache::class.java)
            .map {
                ScreenState.ShowResult(resultRepository.getResult())
            }

        disposable.add(result.subscribe { _state.postValue(Event(it)) })

    }

    fun bindIntentions(intentions: Observable<Intention>) {
        intentions.subscribe(stream)
    }

    override fun onCleared() {
        stream.onComplete()
        disposable.clear()
        super.onCleared()
    }

    sealed class Intention {
        object GetResultCache : Intention()
        object CloseResult : Intention()
    }

    sealed class ScreenState {
        data class ShowResult(
            val pairResult: Pair<SearchViewModel.UserState,
                    List<SearchViewModel.TweetState>>) :
            ScreenState()
    }


}