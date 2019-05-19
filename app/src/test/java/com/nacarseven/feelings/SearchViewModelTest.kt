package com.nacarseven.feelings

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nacarseven.feelings.feature.AndroidSearchMapper
import com.nacarseven.feelings.feature.SearchViewModel
import com.nacarseven.feelings.network.model.TweetsResponse
import com.nacarseven.feelings.repository.ResultRepositoryContract
import com.nacarseven.feelings.repository.SearchRepositoryContract
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var searchRepository: SearchRepositoryContract

    @Mock
    private lateinit var resultRepository: ResultRepositoryContract

    private lateinit var viewModel: SearchViewModel

    private val mapper = AndroidSearchMapper()
    private val moshi = Moshi.Builder().build()

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        viewModel = SearchViewModel(searchRepository, resultRepository, mapper)
    }

    @Test
    fun `should emmit Result state results after screen name valid`() {
        val screenUser = "jakewharton"
        val listTwwetsResponse = getMockedResultSearch()
        val mappedResult = mapper.map(listTwwetsResponse)

        val expected = listOf(SearchViewModel.ScreenState.Loading(true),
            SearchViewModel.ScreenState.Loading(false),
            SearchViewModel.ScreenState.Result(true))
        val actual = mutableListOf<SearchViewModel.ScreenState>()
        val intentions = PublishSubject.create<SearchViewModel.Intention>()

        viewModel.state.observeForever {
            it?.let { state ->
                actual.add(state)
            }
        }

        resultRepository.saveTweetsResult(mappedResult)
        whenever(searchRepository.getSearchResult(screenUser)).thenReturn(Single.just(listTwwetsResponse))
        viewModel.bindIntentions(intentions)
        intentions.onNext(SearchViewModel.Intention.SearchTweets(screenUser))
        assertEquals(expected, actual)

    }

      private fun getMockedResultSearch(): List<TweetsResponse> {
          val result = TestUtil.loadTextFile("fixture/result.json")
          val listResultSearch = Types.newParameterizedType(List::class.java, TweetsResponse::class.java)
          val adapter: JsonAdapter<List<TweetsResponse>> = moshi.adapter(listResultSearch)
          return adapter.fromJson(result)!!
      }

  }