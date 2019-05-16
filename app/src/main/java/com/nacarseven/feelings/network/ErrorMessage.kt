package com.nacarseven.feelings.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException

@JsonClass(generateAdapter = true)
class ErrorMessage(
    @Json(name = "mensagem")
    val message: String?
) {
    companion object {
        val DEFAULT_MESSAGE: String = "Serviço temporariamente indisponível"

        fun parse(it: HttpException): ErrorMessage? {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<ErrorMessage>(ErrorMessage::class.java)
            val json = it.response().errorBody()?.string()

            return try {
                jsonAdapter.fromJson(json)
            } catch (it: IOException) {
                ErrorMessage(DEFAULT_MESSAGE)
            }
        }
    }
}