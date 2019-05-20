package com.nacarseven.feelings.feature.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nacarseven.feelings.network.HttpExceptionHandler
import com.nacarseven.feelings.repository.ResultRepositoryContract
import com.nacarseven.feelings.repository.SearchRepositoryContract
import com.nacarseven.feelings.util.Event
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

private const val HTTP_CODE_NOT_AUTHORIZED = 401

class SearchViewModel(
    private var searchRepository: SearchRepositoryContract,
    private var resultRepository: ResultRepositoryContract,
    private var mapper: SearchMapper
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val stream = PublishSubject.create<Intention>()

    private val _state = MutableLiveData<ScreenState>()
    val state: LiveData<ScreenState>
        get() = _state

    private val _events = MutableLiveData<Event<SideEffect>>()
    val events: LiveData<Event<SideEffect>>
        get() = _events


    init {

        val search: Observable<ScreenState> = stream
            .ofType(Intention.SearchTweets::class.java)
            .switchMap { query ->
                searchRepository
                    .getSearchResult(query.text)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { _state.postValue(
                        ScreenState.Loading(
                            true
                        )
                    ) }
                    .map { mapper.map(it) }
                    .map {
                        resultRepository.saveTweetsResult(it)
                        _state.postValue(
                            ScreenState.Loading(
                                false
                            )
                        )
                        ScreenState.Result(it.second.isNotEmpty()) as ScreenState
                    }
                    .cast(ScreenState::class.java)
                    .onErrorReturn { throwable ->
                        _state.postValue(
                            ScreenState.Loading(
                                false
                            )
                        )
                        val codeError = HttpExceptionHandler.getHttpErrorCode(throwable)
                        if (codeError == HTTP_CODE_NOT_AUTHORIZED) {
                            ScreenState.Error("usuário privado")
                        } else {
                            val errorMessage = HttpExceptionHandler.handleError(throwable)
                            ScreenState.Error(errorMessage)
                        }
                    }
                    .toObservable()

            }

        disposable.add(search.subscribe { _state.postValue(it) })

        val cleanField: Observable<SideEffect> = stream
            .ofType(Intention.ShowErrorMessage::class.java)
            .map { SideEffect.ClearFieldSearch }

        disposable.add(cleanField.subscribe { _events.postValue(Event(it)) })

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
        data class SearchTweets(val text: String) : Intention()
        object ShowErrorMessage : Intention()
    }

    sealed class ScreenState {
        data class Loading(val enable: Boolean) : ScreenState()
        data class Error(val message: CharSequence) : ScreenState()
        data class Result(val hasResultToShow: Boolean) : ScreenState()
    }

    sealed class SideEffect {
        object ClearFieldSearch : SideEffect()
    }

    data class TweetState(
        val date: String,
        val id: String,
        val description: String,
        val replyUser: String
    )

    data class UserState(
        val userName: String,
        val userScreen: String,
        val userImage: String,
        val userLocation: String,
        val userDescription: String,
        val tweetsQtd: String,
        val following: String,
        val followers: String,
        val likes: String,
        val httpUrl: String
    )

}

