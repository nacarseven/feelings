package com.nacarseven.feelings

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.nacarseven.feelings.di.networkModule
import com.nacarseven.feelings.di.repositoryModule
import com.nacarseven.feelings.di.viewModelModule
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import java.io.IOException
import java.net.SocketException

class FeelingsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        setupTimber()
        setupKoin()
        setupRxJavaDefaultErrorHandler()
    }

    private fun setupKoin() {
        startKoin(this, listOf(networkModule, repositoryModule, viewModelModule))
    }

    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }

    private fun setupRxJavaDefaultErrorHandler() {
        RxJavaPlugins.setErrorHandler { error ->
            var e: Throwable? = error
            if (e is UndeliverableException) {
                e = e.cause
            }
            if (e is IOException || e is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (e is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (e is NullPointerException || e is IllegalArgumentException) {
                // that's likely a bug in the application
                val currentThread = Thread.currentThread()
                currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, e)
                return@setErrorHandler
            }
            if (e is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                val currentThread = Thread.currentThread()
                currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, e)
                return@setErrorHandler
            }
            Timber.w("Undeliverable exception received, not sure what to do")
            Timber.w(e)
        }
    }

}