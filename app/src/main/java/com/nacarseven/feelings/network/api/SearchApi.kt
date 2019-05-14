package com.nacarseven.feelings.network.api

import com.nacarseven.feelings.network.model.TweetsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("statuses/user_timeline.json")
    fun getTweets(@Query("screen_name") screenName: String) : Single<List<TweetsResponse>>

//    GET https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2
}