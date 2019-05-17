package com.nacarseven.feelings.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.nacarseven.feelings.R
import com.nacarseven.feelings.extensions.observeNonNull
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_result.*
import org.koin.android.ext.android.inject

class ResultActivity : AppCompatActivity() {

    private val viewModel: ResultViewModel by inject()
    private val resultAdapter = ResultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        observeViewModel()
        setUpRecyclerView()

    }

    private fun observeViewModel() {
        viewModel.bindIntentions(intentions())

        viewModel.state.observeNonNull(this) {

            it.getContentIfNotHandled()?.let {
                when (it) {
                    is ResultViewModel.ScreenState.ShowResult -> {
                        resultAdapter.list = it.pairResult.second
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        with(recyclerView) {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(this)
        }
    }

    private fun intentions(): Observable<ResultViewModel.Intention> {
        return RxView
            .clicks(imgClose)
            .map {
                ResultViewModel.Intention.CloseResult
            }
    }

}