package com.nacarseven.feelings.di

import android.content.Context
import android.net.wifi.WifiManager
import com.nacarseven.feelings.base.SessionTimeout
import com.nacarseven.feelings.base.SessionTimeoutHandler
import com.nacarseven.feelings.network.api.SearchApi
import com.nacarseven.feelings.repository.SearchRepositoryContract
import com.nacarseven.feelings.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigInteger
import java.net.Inet4Address
import java.net.UnknownHostException
import java.nio.ByteOrder
import java.util.concurrent.TimeUnit

const val INTERCEPTOR_LOGGING = "logging"
const val INTERCEPTOR_AUTHORIZATION = "authorization"
const val INTERCEPTOR_FORWARD_IP = "interceptorForwardIp"
const val INTERCEPTOR_NETWORK_DISCONNECTED = "interceptorNetworkDisconnected"
const val INTERCEPTOR_VERSION_CODE = "interceptorVersionCode"
const val HEADER_AUTHORIZATION = "Authorization"
const val PROPERTY_BASE_URL = "baseUrl"
const val AUTHENTICATOR = "authenticator"
const val AUTHENTICATOR_OKHTTP = "authenticatorOkHttp"
const val OKHTTP = "okhttp"
const val RETROFIT_LOGIN = "retrofitLogin"
const val RETROFIT = "retrofit"
const val STREAM_SESSION_TIMEOUT = "streamSessionTimeout"
const val STREAM_SESSION_TIMEOUT_HANDLER = "streamSessionTimeoutHandler"
const val WIFI_IP = "networkManager"

val networkModule = module {

    factory(named(WIFI_IP)) {
        val context = androidApplication() as Context
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var ipAddress = wifiManager.connectionInfo.ipAddress

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
            ipAddress = Integer.reverseBytes(ipAddress)

        val ipByteArray = BigInteger
            .valueOf(ipAddress.toLong())
            .toByteArray()

        try {
            Inet4Address.getByAddress(ipByteArray).hostAddress
        } catch (exception: UnknownHostException) {
            Constants.EMPTY_STRING
        }
    }

    single(named(STREAM_SESSION_TIMEOUT)) {
        SessionTimeout()
    }

    single(named(STREAM_SESSION_TIMEOUT_HANDLER)) {
        SessionTimeoutHandler(get())
    }

    single(named(INTERCEPTOR_AUTHORIZATION)) {
        val searchRepository: SearchRepositoryContract = get()
        Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader(HEADER_AUTHORIZATION, "bearer ${searchRepository.getAccessToken()}")
            chain.proceed(builder.build())
        }
    }

    single {
        val retrofit: Retrofit = get(named(RETROFIT))
        retrofit.create(SearchApi::class.java)
    }

    single(named(OKHTTP)) {
        OkHttpClient
            .Builder()
            .connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .authenticator(get(named(AUTHENTICATOR)))
            .addInterceptor(get(named(INTERCEPTOR_AUTHORIZATION)))
            .addInterceptor(get(named(INTERCEPTOR_LOGGING)))
            .addInterceptor(get(named(INTERCEPTOR_NETWORK_DISCONNECTED)))
            .build()
    }

    single(named(AUTHENTICATOR_OKHTTP)) {
        OkHttpClient
            .Builder()
            .connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(get(named(INTERCEPTOR_FORWARD_IP)))
            .addInterceptor(get(named(INTERCEPTOR_NETWORK_DISCONNECTED)))
            .addInterceptor(get(named(INTERCEPTOR_LOGGING)))
            .addInterceptor(get(named(INTERCEPTOR_VERSION_CODE)))
            .build()
    }

    single(named(RETROFIT)) {
        val baseUrl = getProperty<String>(PROPERTY_BASE_URL)
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(get(named(OKHTTP)))
            .build()
    }

    single(named(RETROFIT)) {
        val baseUrl = getProperty<String>(PROPERTY_BASE_URL)
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(get(named(OKHTTP)))
            .build()
    }

    single(named(RETROFIT_LOGIN)) {
        val baseUrl = getProperty<String>(PROPERTY_BASE_URL)
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .client(get(named(AUTHENTICATOR_OKHTTP)))
            .build()
    }

}


