package com.nacarseven.feelings.network.api

import retrofit2.http.GET

interface SearchApi {

    @GET("https://api.twitter.com/1.1/statuses/user_timeline.json")
    fun getTweets()

//    GET https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterapi&count=2
}