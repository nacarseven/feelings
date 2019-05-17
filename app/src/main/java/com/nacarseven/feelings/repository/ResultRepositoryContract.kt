package com.nacarseven.feelings.repository

import com.nacarseven.feelings.feature.SearchViewModel

interface ResultRepositoryContract {

    fun saveTweetsResult(pairResult: Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>>)

    fun getResult(): Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>>

    fun deleteResultCache()
}