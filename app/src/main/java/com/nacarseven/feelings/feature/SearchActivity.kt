package com.nacarseven.feelings.feature

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.nacarseven.feelings.R
import com.nacarseven.feelings.extensions.observeNonNull
import io.reactivex.Observable
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
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

    }


}