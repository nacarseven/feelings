package com.nacarseven.feelings.repository

import com.nacarseven.feelings.network.api.SearchApi
import com.nacarseven.feelings.network.model.TweetsResponse
import io.reactivex.Single

class SearchRepository(private var searchApi: SearchApi) : SearchRepositoryContract {

    override fun getSearchResult(query: String): Single<List<TweetsResponse>> {
        return searchApi.getTweets(query)
    }

}