package com.nacarseven.feelings.feature

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.nacarseven.feelings.R
import com.nacarseven.feelings.extensions.observeNonNull
import io.reactivex.Observable
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.bindIntentions(intentions())
        viewModel.state.observeNonNull(this) { state ->

            if (state === SearchViewModel.ScreenState.Loading) {

            } else {

            }

            if (state !== SearchViewModel.ScreenState.Empty) {

            } else {

            }

            if (state is SearchViewModel.ScreenState.Error) {

            } else {

            }

            when (state) {

                is SearchViewModel.ScreenState.Empty -> renderEmptyState()

                is SearchViewModel.ScreenState.Result -> renderAutocompleteResults(state)
            }

            Timber.e("Rendered state: $state")
        }
    }

    private fun renderEmptyState() {

    }

    private fun renderAutocompleteResults(state: SearchViewModel.ScreenState.Result) {

    }

    private fun intentions(): Observable<SearchViewModel.Intention> {
        val typing = RxTextView
            .textChanges(editTextSearchUserTimeline)
            .skipInitialValue()
            .map { SearchViewModel.Intention.SearchTweets(it.toString()) }

        val clearField = RxView
            .clicks(buttonCleanEditTextSearch)
            .map {
                SearchViewModel.Intention.ClearSearch }

        return Observable.merge(listOf(typing, clearField))

    }


}
