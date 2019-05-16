package com.nacarseven.feelings.repository

import com.nacarseven.feelings.network.api.SearchApi
import com.nacarseven.feelings.network.model.TweetsResponse
import io.reactivex.Single

const val TOKEN =
    "AAAAAAAAAAAAAAAAAAAAAC2J%2BQAAAAAAX5B58aZYrknMxkcsseexgaLO2Ec%3DN2WSSTifrM1VvmsfzzuDdSLHKEymLHSzmhHd36RQ1XYLtdkuFD"


class SearchRepository(private var searchApi: SearchApi) : SearchRepositoryContract {


    override fun getAccessToken(): String {
        return TOKEN
    }

    override fun getSearchResult(query: String): Single<List<TweetsResponse>> {
        return searchApi.getTweets(query)
    }

}