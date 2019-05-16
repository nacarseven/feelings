package com.nacarseven.feelings.feature

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Parcelable
import com.nacarseven.feelings.network.HttpExceptionHandler
import com.nacarseven.feelings.network.model.TweetsResponse
import com.nacarseven.feelings.network.model.User
import com.nacarseven.feelings.repository.SearchRepositoryContract
import com.nacarseven.feelings.util.Event
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class SearchViewModel(
    private var searchRepository: SearchRepositoryContract,
    private var mapper: SearchMapper
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
                    .map { mapper.map(it) }
                    .map { ScreenState.Result(it.first, it.second) as ScreenState }
                    .cast(ScreenState::class.java)
                    .onErrorReturn {
                            throwable ->
                        val errorMessage = HttpExceptionHandler.handleError(throwable)
                        ScreenState.Error(errorMessage)
                    }
                    .toObservable()

            }

        disposables.add(search.subscribe { _state.postValue(it) })

        val cleanField: Observable<SideEffect> = stream
            .ofType(Intention.ShowErrorMessage::class.java)
            .map { SideEffect.ClearFieldSearch }

        disposables.add(cleanField.subscribe { _events.postValue(Event(it))})

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
        object ShowErrorMessage : Intention()
    }

    sealed class ScreenState {
        object Empty : ScreenState()
        object Loading : ScreenState()
        data class Error(val message: CharSequence) : ScreenState()
        data class Result(val user: UserState, val tweetList: List<TweetState>) : ScreenState()
    }

    sealed class SideEffect {
        object ClearFieldSearch : SideEffect()
    }

    data class TweetState(
        val date: String,
        val id: String,
        val description: String,
        val replyUser: String?,
        val likes: String,
        val retweets: String
    )

    data class UserState(
        val userName: String,
        val userScreen: String,
        val userImage: String,
        val userLocation: String,
        val userDescription: String,
        val tweetsQtd: String,
        val following: String
    )

}

