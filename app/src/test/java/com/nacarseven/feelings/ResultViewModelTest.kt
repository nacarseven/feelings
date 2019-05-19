package com.nacarseven.feelings

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nacarseven.feelings.feature.AndroidSearchMapper
import com.nacarseven.feelings.feature.ResultViewModel
import com.nacarseven.feelings.network.model.TweetsResponse
import com.nacarseven.feelings.repository.ResultRepositoryContract
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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
class ResultViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var resultRepository: ResultRepositoryContract

    private lateinit var viewModel: ResultViewModel

    private val moshi = Moshi.Builder().build()
    private val mapper = AndroidSearchMapper()

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        viewModel = ResultViewModel(resultRepository)
    }

    @Test
    fun `should emmit Result state results after screen name valid`() {
        val listTwwetsResponse = getMockedResultSearch()
        val mappedResult = mapper.map(listTwwetsResponse)

        val expected = listOf(
            ResultViewModel.ScreenState.ShowResult(mappedResult)
        )
        val actual = mutableListOf<ResultViewModel.ScreenState>()
        val intentions = PublishSubject.create<ResultViewModel.Intention>()

        viewModel.state.observeForever {
            it?.let { state ->
                actual.add(state.peekContent())
            }
        }

        whenever(resultRepository.getResult()).thenReturn(mappedResult)

        viewModel.bindIntentions(intentions)
        intentions.onNext(ResultViewModel.Intention.GetResultCache)
        assertEquals(expected, actual)

    }

    private fun getMockedResultSearch(): List<TweetsResponse> {
        val result = TestUtil.loadTextFile("fixture/result.json")
        val listResultSearch = Types.newParameterizedType(List::class.java, TweetsResponse::class.java)
        val adapter: JsonAdapter<List<TweetsResponse>> = moshi.adapter(listResultSearch)
        return adapter.fromJson(result)!!
    }

}
