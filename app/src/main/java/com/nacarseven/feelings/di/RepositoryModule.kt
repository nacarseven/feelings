package com.nacarseven.feelings.di

import com.nacarseven.feelings.repository.ResultRepository
import com.nacarseven.feelings.repository.ResultRepositoryContract
import com.nacarseven.feelings.repository.SearchRepository
import com.nacarseven.feelings.repository.SearchRepositoryContract
import org.koin.dsl.module.module

val repositoryModule = module {

    single { SearchRepository(get("retrofit_twitter")) as SearchRepositoryContract }
    single { ResultRepository(get("retrofit_google")) as ResultRepositoryContract }

}