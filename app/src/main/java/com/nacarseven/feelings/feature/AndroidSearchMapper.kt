package com.nacarseven.feelings.feature

import com.nacarseven.feelings.network.model.TweetsResponse

interface SearchMapper {
    fun map(result: List<TweetsResponse>) : Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>>

}

class AndroidSearchMapper : SearchMapper {

    override fun map(result: List<TweetsResponse>) : Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>> {

        val user = result[0].user
        val tweetStateList = arrayListOf<SearchViewModel.TweetState>()

        val userState = SearchViewModel.UserState(
            user.name,
            user.screenName,
            user.profileImage,
            user.location,
            user.descriptionUser,
            user.tweets.toString(),
            user.following.toString()
        )

        for(item in result){
            tweetStateList.add(SearchViewModel.TweetState(item.createDate,
                item.id.toString(),
                item.description,
                item.replyUser!!,
                item.likes.toString(),
                item.retweets.toString()))
        }

        return Pair(userState, tweetStateList)

    }

}