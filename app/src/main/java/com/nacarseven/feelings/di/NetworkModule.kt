package com.nacarseven.feelings.di

import com.google.gson.GsonBuilder
import com.nacarseven.feelings.BuildConfig
import com.nacarseven.feelings.network.api.SearchApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIMEOUT = 120L

val networkModule =  module(definition = {
        // provided web components
        single { createOkHttpClient() }
        // Fill property
        single {
            createWebService<SearchApi>(get(), BuildConfig.BASE_URL) }
    })

    fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()
        return retrofit.create(T::class.java)
    }

//single(named(INTERCEPTOR_AUTHORIZATION)) {
//        val searchRepository: SearchRepositoryContract = get()
//        Interceptor { chain ->
//            val builder = chain.request().newBuilder()
//            builder.addHeader(HEADER_AUTHORIZATION, "bearer ${searchRepository.getAccessToken()}")
//            chain.proceed(builder.build())
//        }
//    }


//
//    single {
//        val retrofit: Retrofit = get(named(RETROFIT))
//        retrofit.create(SearchApi::class.java)
//    }
//



