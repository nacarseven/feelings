package com.nacarseven.feelings.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.view.RxView
import com.nacarseven.feelings.R
import com.nacarseven.feelings.extensions.observeNonNull
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.layout_user_profile.*
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
                        setupUserProfileLayout(it.pairResult.first)
                        setListAdapter(it.pairResult.second)
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        with(recyclerViewTweetsResult) {
            adapter = resultAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setListAdapter(items: List<SearchViewModel.TweetState>) {
        resultAdapter.list = items
    }

    private fun setupUserProfileLayout(user: SearchViewModel.UserState) {
//        civUserProfile.setImageBitmap(user.userImage)
    }

    private fun intentions(): Observable<ResultViewModel.Intention> {

        val checkResult = Observable
            .just(ResultViewModel.Intention.GetResultCache)

        val closeScreen = RxView
            .clicks(imgClose)
            .map {
                ResultViewModel.Intention.CloseResult
            }

        return Observable.merge(checkResult, closeScreen)
    }

}