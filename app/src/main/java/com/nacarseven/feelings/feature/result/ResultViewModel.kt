package com.nacarseven.feelings.feature.result

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nacarseven.feelings.feature.search.SearchViewModel
import com.nacarseven.feelings.network.HttpExceptionHandler
import com.nacarseven.feelings.network.model.FeelingResponse
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

        val evaluateFeeling: Observable<ScreenState> = stream
            .ofType(Intention.EvaluateFeelingItem::class.java)
            .switchMap { query ->
                resultRepository.evaluateFeelings(query.text)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe{ _state.postValue(Event(
                        ScreenState.Loading(
                            true
                        )
                    ))}
                    .map {
                        _state.postValue(Event(
                            ScreenState.Loading(
                                false
                            )
                        ))
                        ScreenState.ShowFeeling(it) as ScreenState
                    }
                    .cast(ScreenState::class.java)
                    .onErrorReturn {
                            throwable ->
                        _state.postValue(Event(
                            ScreenState.Loading(
                                false
                            )
                        ))
                        val errorMessage = HttpExceptionHandler.handleError(throwable)
                        ScreenState.Error(errorMessage)
                    }
                    .toObservable()
            }

        disposable.add(Observable.merge(result, evaluateFeeling).subscribe { _state.postValue(Event(it))})

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
        data class EvaluateFeelingItem(val text: String, val position: Int) : Intention()
    }

    sealed class ScreenState {
        data class Error(val message: CharSequence) : ScreenState()
        data class Loading(val show: Boolean) : ScreenState()
        data class ShowResult(
            val pairResult: Pair<SearchViewModel.UserState,
                    List<SearchViewModel.TweetState>>) :
            ScreenState()
        data class ShowFeeling(val score: FeelingResponse) : ScreenState()
    }


}