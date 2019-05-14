package com.nacarseven.feelings.di

import com.nacarseven.feelings.feature.SearchViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val viewModelModule = applicationContext {

    viewModel { SearchViewModel(get()) }

}
