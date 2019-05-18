package com.nacarseven.feelings.repository

import com.nacarseven.feelings.feature.SearchViewModel
import com.nacarseven.feelings.network.api.FeelingsApi
import com.nacarseven.feelings.network.model.DocumentData
import com.nacarseven.feelings.network.model.FeelingData
import com.nacarseven.feelings.network.model.FeelingResponse
import com.orhanobut.hawk.Hawk
import io.reactivex.Single

private const val USER = "user"
private const val TWEETS = "tweets"

class ResultRepository(private var feelingApi: FeelingsApi) : ResultRepositoryContract {

    override fun evaluateFeelings(text: String): Single<FeelingResponse> {
        val language = "PT"
        val doc = DocumentData("PLAIN_TEXT", language, text)
        val data = FeelingData(doc, "UTF8")
        val key = "AIzaSyC5DBefLY_EYjKNd9R4YueVYc9mG430eqY"

        return feelingApi.evaluateFeelings(key, data)
    }

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
