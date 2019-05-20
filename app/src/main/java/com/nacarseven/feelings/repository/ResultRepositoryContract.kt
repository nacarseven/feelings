package com.nacarseven.feelings.repository

import com.nacarseven.feelings.feature.search.SearchViewModel
import com.nacarseven.feelings.network.model.FeelingResponse
import io.reactivex.Single

interface ResultRepositoryContract {

    fun saveTweetsResult(pairResult: Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>>)

    fun getResult(): Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>>

    fun deleteResultCache()

    fun evaluateFeelings(text: String) : Single<FeelingResponse>
}