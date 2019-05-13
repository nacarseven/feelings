package com.nacarseven.feelings.repository

import com.nacarseven.feelings.network.model.TweetsResponse
import io.reactivex.Single

interface SearchRepositoryContract {

    fun getAccessToken(): String

    fun getSearchResult(userTimeline: String) : Single<List<TweetsResponse>>

}