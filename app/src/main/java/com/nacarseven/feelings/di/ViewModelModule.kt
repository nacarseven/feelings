package com.nacarseven.feelings.di

import com.nacarseven.feelings.feature.SearchViewModel
import org.koin.dsl.module

val viewModelModule = module {

    single { SearchViewModel(get()) }

}
