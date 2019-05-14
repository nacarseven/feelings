package com.nacarseven.feelings.di

import org.koin.dsl.module.applicationContext
import java.util.concurrent.Executor
import java.util.concurrent.Executors

const val EXECUTOR = "executor"

val androidModule = applicationContext {

    bean(EXECUTOR) { Executors.newSingleThreadExecutor() as Executor }
}