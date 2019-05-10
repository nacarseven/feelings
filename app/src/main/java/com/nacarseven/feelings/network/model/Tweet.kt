package com.nacarseven.feelings.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tweet(
    @Json(name = "text")
    val description: String,

    @Json(name = "created_at")
    var createDate: String?,

    @Json(name = "user")
    var user: User,

    @Json(name = "retweet_count")
    var retweetCount: Int

)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "name")
    val name: String,

    @Json(name = "screen_name")
    var screenName: String,

    @Json(name = "profile_background_image_url")
    var profileImage: String

)