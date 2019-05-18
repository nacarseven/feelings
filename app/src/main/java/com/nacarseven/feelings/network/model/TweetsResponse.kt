package com.nacarseven.feelings.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TweetsResponse(

    @Json(name = "created_at")
    val createDate: String,

    val id: Long,

    @Json(name = "text")
    val description: String,

    val user: User,

    @Json(name = "in_reply_to_screen_name")
    val replyUser: String?,

    @Json(name = "retweet_count")
    val retweets: Int

)

@JsonClass(generateAdapter = true)
data class User(
    val name: String,

    @Json(name = "screen_name")
    val screenName: String,

    @Json(name = "profile_image_url")
    val profileImage: String,

    val location: String,

    @Json(name = "description")
    val descriptionUser: String,

    @Json(name = "statuses_count")
    val tweets: Int,

    @Json(name = "friends_count")
    val following: Int,

    @Json(name = "followers_count")
    val followers: Int,

    @Json(name = "favourites_count")
    val likes: Int,

    @Json(name = "url")
    val httpUrl : String

)