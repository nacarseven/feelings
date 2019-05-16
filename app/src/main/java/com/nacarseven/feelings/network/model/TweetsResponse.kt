package com.nacarseven.feelings.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TweetsResponse(

    @Json(name = "created_at")
    var createDate: String,

    @Json(name = "id")
    var id: Long,

    @Json(name = "text")
    val description: String,

    @Json(name = "user")
    var user: User,

    @Json(name = "retweet_count")
    var retweetCount: Int,

    @Json(name = "in_reply_to_screen_name")
    var replyUser: String,

    @Json(name = "favourites_count")
    var likes: Int,

    @Json(name = "retweet_count")
    var retweets: Int

)

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "name")
    val name: String,

    @Json(name = "screen_name")
    var screenName: String,

    @Json(name = "profile_image_url")
    var profileImage: String,

    @Json(name = "location")
    var location: String,

    @Json(name = "description")
    var descriptionUser: String,

    @Json(name = "statuses_count")
    var tweets: Int,

    @Json(name = "friends_count")
    var following: Int

)