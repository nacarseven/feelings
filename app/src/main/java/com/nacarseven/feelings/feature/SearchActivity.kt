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

private const val ALPHA_OPAQUE = 1f
private const val ALPHA_HALF_TRANSPARENT = 0.5f

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        renderAutocompleteResults(null)
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.bindIntentions(intentions())
        viewModel.events.observeNonNull(this) {
            it.getContentIfNotHandled()?.let { sideEffect ->
                when (sideEffect) {
//                    is SearchViewModel.SideEffect.ClearFieldSearch ->
//                        editTextSearch.text = null
                    is SearchViewModel.SideEffect.EmptyValueSearch ->
                        textInputLayout.error = "digite um usuario válido"
                }

            }


        }


        viewModel.state.observeNonNull(this) { state ->

            if (state === SearchViewModel.ScreenState.Loading) {

            } else {

            }

            if (state !== SearchViewModel.ScreenState.Empty) {

            } else {

            }

            if (state is SearchViewModel.ScreenState.Error) {
                textInputLayout.error = state.message
//                buttonSearch.error = true
            } else {
                textInputLayout.error?.let { textInputLayout.error = null }
//                buttonSearch.error = false
            }

            when (state) {
                is SearchViewModel.ScreenState.Empty -> renderEmptyState()
                is SearchViewModel.ScreenState.Result -> renderAutocompleteResults(state)
            }

            Timber.e("Rendered state: $state")
        }
    }

    private fun renderEmptyState() {
        if (!editTextSearch.text.isNullOrBlank())
            editTextSearch.text = null

        buttonSearch.alpha = ALPHA_HALF_TRANSPARENT
        buttonSearch.isEnabled = false
    }

    private fun renderAutocompleteResults(state: SearchViewModel.ScreenState.Result?) {
        buttonSearch.alpha = ALPHA_OPAQUE
        buttonSearch.isEnabled = true
    }

    private fun intentions(): Observable<SearchViewModel.Intention> {
        val typing = RxTextView
            .textChanges(editTextSearch)
            .skipInitialValue()
            .map { null }

        val searchAndClearText = RxView
            .clicks(buttonSearch)
            .map {
                if (!editTextSearch.text.isNullOrBlank()) SearchViewModel.Intention.SearchTweets(editTextSearch.text.toString())
                else SearchViewModel.Intention.EmptyValue
            }

        return Observable.merge(listOf(searchAndClearText, null))

    }


}
