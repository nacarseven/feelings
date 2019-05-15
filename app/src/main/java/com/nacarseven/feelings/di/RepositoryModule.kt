package com.nacarseven.feelings.di

import com.nacarseven.feelings.repository.SearchRepository
import com.nacarseven.feelings.repository.SearchRepositoryContract
import org.koin.dsl.module.module

val repositoryModule = module {

    single { SearchRepository(get()) as SearchRepositoryContract }

}