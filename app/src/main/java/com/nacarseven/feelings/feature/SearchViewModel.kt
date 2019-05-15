package com.nacarseven.feelings.feature

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nacarseven.feelings.network.model.TweetsResponse
import com.nacarseven.feelings.repository.SearchRepositoryContract
import com.nacarseven.feelings.util.Event
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class SearchViewModel(
    private var searchRepository: SearchRepositoryContract
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val stream = PublishSubject.create<Intention>()

    private val _state = MutableLiveData<ScreenState>()
    val state: LiveData<ScreenState>
        get() = _state

    private val _events = MutableLiveData<Event<SideEffect>>()
    val events: LiveData<Event<SideEffect>>
        get() = _events


    init {
        _state.value = ScreenState.Empty

        val search: Observable<ScreenState> = stream
            .ofType(Intention.SearchTweets::class.java)
            .switchMap { query ->
                searchRepository
                    .getSearchResult(query.text)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { _state.postValue(ScreenState.Loading) }
                    .map { ScreenState.Result(it) as ScreenState }
                    .onErrorReturn { ScreenState.Result(emptyList()) }
                    .toObservable()

            }

        disposables.add(search.subscribe { _state.postValue(it) })


    }

    fun bindIntentions(intentions: Observable<Intention>) {
        intentions.subscribe(stream)
    }

    override fun onCleared() {
        stream.onComplete()
        disposables.clear()
        super.onCleared()
    }


    sealed class Intention {
        data class SearchTweets(val text: String) : Intention()
        object EmptyValue : Intention()
        object ClearSearch : Intention()

    }

    sealed class ScreenState {
        object Empty : ScreenState()
        object Loading : ScreenState()
        data class Error(val message: CharSequence) : ScreenState()
        data class Result(val tweetList: List<TweetsResponse>) : ScreenState()
    }

    sealed class SideEffect {
        object EmptyValueSearch : SideEffect()
        object ClearFieldSearch : SideEffect()
    }

}

