package com.nacarseven.feelings.di

import com.nacarseven.feelings.repository.SearchRepository
import com.nacarseven.feelings.repository.SearchRepositoryContract
import org.koin.dsl.module.applicationContext

val repositoryModule = applicationContext {

    bean { SearchRepository(get()) as SearchRepositoryContract }
}