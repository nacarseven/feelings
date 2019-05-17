package com.nacarseven.feelings.repository

import com.nacarseven.feelings.feature.SearchViewModel
import com.orhanobut.hawk.Hawk

private const val USER = "user"
private const val TWEETS = "tweets"

class ResultRepository : ResultRepositoryContract {

    override fun deleteResultCache() {
        Hawk.delete(USER)
        Hawk.delete(TWEETS)
    }

    override fun getResult(): Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>> {
        val user = Hawk.get<SearchViewModel.UserState>(USER)
        val list = Hawk.get<List<SearchViewModel.TweetState>>(TWEETS)
        return Pair(user, list)
    }

    override fun saveTweetsResult(pairResult: Pair<SearchViewModel.UserState, List<SearchViewModel.TweetState>>) {
        Hawk.put(USER, pairResult.first)
        Hawk.put(TWEETS, pairResult.second)
    }

}
