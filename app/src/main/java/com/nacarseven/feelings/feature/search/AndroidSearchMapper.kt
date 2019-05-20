package com.nacarseven.feelings.feature.search

import android.annotation.SuppressLint
import com.nacarseven.feelings.network.model.TweetsResponse
import java.text.SimpleDateFormat
import java.util.*

interface SearchMapper {
    fun map(result: List<TweetsResponse>): Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>>

}

class AndroidSearchMapper : SearchMapper {

    @SuppressLint("SimpleDateFormat")
    override fun map(result: List<TweetsResponse>): Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>> {

        val user = result[0].user
        val tweetStateList = arrayListOf<SearchViewModel.TweetState>()
        val simpleDateFormat = SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")

        val userState = SearchViewModel.UserState(
            user.name,
            user.screenName,
            user.profileImage,
            user.location,
            user.descriptionUser,
            user.tweets.toString(),
            user.following.toString(),
            user.followers.toString(),
            user.likes.toString(),
            user.httpUrl
        )


        for (item in result) {

            val date = simpleDateFormat.parse(item.createDate)
            val stringFormatted = formatter.format(date)

            tweetStateList.add(
                SearchViewModel.TweetState(
                    stringFormatted,
                    item.id.toString(),
                    item.description,
                    if (item.replyUser.isNullOrEmpty()) "0" else item.replyUser
                )
            )
        }

        return Pair(userState, tweetStateList)

    }
}