package com.nacarseven.feelings.di

import com.nacarseven.feelings.feature.SearchViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel { SearchViewModel(get()) }

}
