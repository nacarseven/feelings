package com.nacarseven.feelings.di

import com.nacarseven.feelings.feature.search.AndroidSearchMapper
import com.nacarseven.feelings.feature.result.ResultViewModel
import com.nacarseven.feelings.feature.search.SearchMapper
import com.nacarseven.feelings.feature.search.SearchViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    factory { AndroidSearchMapper() as SearchMapper }

    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { ResultViewModel(get()) }

}
