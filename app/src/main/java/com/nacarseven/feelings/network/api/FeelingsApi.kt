package com.nacarseven.feelings.network.api

import com.nacarseven.feelings.network.model.FeelingData
import com.nacarseven.feelings.network.model.FeelingResponse
import com.nacarseven.feelings.network.model.TweetsResponse
import io.reactivex.Single
import retrofit2.http.*

interface FeelingsApi {

    @Headers("Content-Type: application/json")
    @POST(" documents:analyzeSentiment")
    fun evaluateFeelings(
        @Query("key") key: String,
        @Body data: FeelingData
    ): Single<FeelingResponse>

}