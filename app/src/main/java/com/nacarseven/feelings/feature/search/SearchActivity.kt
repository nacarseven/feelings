package com.nacarseven.feelings.feature.search

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.nacarseven.feelings.R
import com.nacarseven.feelings.extensions.observeNonNull
import com.nacarseven.feelings.feature.result.ResultActivity
import io.reactivex.Observable
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
        viewModel.events.observeNonNull(this) {
            it.getContentIfNotHandled()?.let { sideEffect ->
                when (sideEffect) {
                    is SearchViewModel.SideEffect.ClearFieldSearch ->
                        textInputLayout.error = resources.getString(R.string.error_user)
                }
            }
        }

        viewModel.state.observeNonNull(this) { state ->
            if (state is SearchViewModel.ScreenState.Error) {
                textInputLayout.error = state.message
                buttonSearch.error = true
            } else {
                textInputLayout.error?.let { textInputLayout.error = null }
                buttonSearch.error = false
            }

            when (state) {
                is SearchViewModel.ScreenState.Result -> goToActivityResult(state)
                is SearchViewModel.ScreenState.Loading -> {
                    progressSearch.visibility = if (state.enable) View.VISIBLE else View.GONE
                }
            }

            Timber.e("Rendered state: $state")
        }
    }

    private fun goToActivityResult(state: SearchViewModel.ScreenState.Result) {
        if (state.hasResultToShow) {
            Intent(this, ResultActivity::class.java).apply {
                startActivity(this)
                editTextSearch.text = null
                progressSearch.visibility = View.GONE
            }
        }

    }


    private fun intentions(): Observable<SearchViewModel.Intention> {
        return RxView
            .clicks(buttonSearch)
            .map {
                if (buttonSearch.error) {
                    editTextSearch.text = null
                    buttonSearch.error = !buttonSearch.error
                    SearchViewModel.Intention.ShowErrorMessage
                } else {
                    SearchViewModel.Intention.SearchTweets(editTextSearch.text.toString())
                }

            }
    }


}
