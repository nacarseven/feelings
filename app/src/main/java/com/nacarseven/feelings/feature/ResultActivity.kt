package com.nacarseven.feelings.feature

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.view.RxView
import com.nacarseven.feelings.R
import com.nacarseven.feelings.extensions.loadImage
import com.nacarseven.feelings.extensions.observeNonNull
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.layout_user_location.*
import kotlinx.android.synthetic.main.layout_user_profile.*
import kotlinx.android.synthetic.main.layout_user_tweet_values.*
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
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            val spacingDecoration = DividerItemDecoration(context, manager.orientation)
            addItemDecoration(spacingDecoration)
        }
    }

    private fun setListAdapter(items: List<SearchViewModel.TweetState>) {
        resultAdapter.list = items
    }

    private fun setupUserProfileLayout(user: SearchViewModel.UserState) {
        civUserProfile.loadImage(user.userImage)
        txtUserName.text = user.userName
        txtScreenName.text = user.userScreen
        txtDescriptionProfile.text = user.userDescription
        txtLocation.text = user.userLocation
        txtLink.text = user.httpUrl
        txtQtFollowers.text = user.followers
        txtQtFollowing.text = user.following
        txtQtLikes.text = user.likes
        txtQtTwwets.text = user.tweetsQtd
    }

    private fun intentions(): Observable<ResultViewModel.Intention> {

        val init = Observable
            .just(ResultViewModel.Intention.GetResultCache)

        val clickItemList = resultAdapter
            .clickedTweet()
            .map {
                ResultViewModel.Intention.EvaluateFeelingItem(it.description)
            }

        return Observable.merge(init, clickItemList)

    }

}