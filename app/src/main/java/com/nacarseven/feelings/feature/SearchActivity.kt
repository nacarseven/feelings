package com.nacarseven.feelings.feature

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.nacarseven.feelings.R
import com.nacarseven.feelings.extensions.observeNonNull
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject
import timber.log.Timber

private const val ALPHA_OPAQUE = 1f
private const val ALPHA_HALF_TRANSPARENT = 0.5f

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.bindIntentions(intentions())
        viewModel.events.observeNonNull(this) {
            it.getContentIfNotHandled()?.let { sideEffect ->
                when (sideEffect) {
                    is SearchViewModel.SideEffect.ClearFieldSearch ->
                        textInputLayout.error = "digite um usuario válido"
                }
            }
        }

        viewModel.state.observeNonNull(this) { state ->

            if (state === SearchViewModel.ScreenState.Loading) {
                progressSearch.visibility = View.VISIBLE
            } else {
                progressSearch.visibility = View.GONE
            }

            if (state !== SearchViewModel.ScreenState.Empty) {

            } else {

            }

            if (state is SearchViewModel.ScreenState.Error) {
                textInputLayout.error = state.message
                buttonSearch.error = true
            } else {
                textInputLayout.error?.let { textInputLayout.error = null }
                buttonSearch.error = false
            }

            when (state) {
                is SearchViewModel.ScreenState.Empty -> renderEmptyState()
                is SearchViewModel.ScreenState.Result -> renderAutocompleteResults(state)
            }

            Timber.e("Rendered state: $state")
        }
    }

    private fun renderEmptyState() {
        layoutNotFoundResult.visibility = View.VISIBLE
//        if (!editTextSearch.text.isNullOrBlank())
//            editTextSearch.text = null
//
//        buttonSearch.alpha = ALPHA_HALF_TRANSPARENT
//        buttonSearch.isEnabled = false
    }

    private fun renderAutocompleteResults(state: SearchViewModel.ScreenState.Result?) {
        layoutNotFoundResult.visibility = if(state?.tweetList?.size == 0) View.VISIBLE else View.GONE
//        buttonSearch.alpha = ALPHA_OPAQUE
//        buttonSearch.isEnabled = true

    }


    private fun intentions(): Observable<SearchViewModel.Intention> {
        return RxView
            .clicks(buttonSearch)
            .map {
                if (!editTextSearch.text.isNullOrBlank()) SearchViewModel.Intention.SearchTweets(editTextSearch.text.toString())
                else SearchViewModel.Intention.ShowErrorMessage
            }


    }


}
