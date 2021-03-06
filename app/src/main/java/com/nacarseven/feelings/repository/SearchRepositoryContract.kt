package com.nacarseven.feelings.repository

import com.nacarseven.feelings.network.model.TweetsResponse
import io.reactivex.Single

interface SearchRepositoryContract {

    fun getSearchResult(query: String) : Single<List<TweetsResponse>>

}