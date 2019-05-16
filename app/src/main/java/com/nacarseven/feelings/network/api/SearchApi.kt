package com.nacarseven.feelings.network.api

import com.nacarseven.feelings.network.model.TweetsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SearchApi {

    @Headers("AUTHORIZATION: bearer AAAAAAAAAAAAAAAAAAAAAC2J%2BQAAAAAAX5B58aZYrknMxkcsseexgaLO2Ec%3DN2WSSTifrM1VvmsfzzuDdSLHKEymLHSzmhHd36RQ1XYLtdkuFD")
    @GET("statuses/user_timeline.json")
    fun getTweets(@Query("screen_name") screenName: String) : Single<List<TweetsResponse>>

}