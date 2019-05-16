package com.nacarseven.feelings.network

import retrofit2.HttpException

object HttpExceptionHandler {
    fun handleError(it: Throwable): String {
        var message = ErrorMessage.DEFAULT_MESSAGE
        if (it is HttpException) {
            message = ErrorMessage.parse(it)?.message ?: ErrorMessage.DEFAULT_MESSAGE
        }
        return message
    }

    fun getHttpErrorCode(it: Throwable): Int {
        var code = Int.MIN_VALUE
        if (it is HttpException) {
            code = it.code()
        }
        return code
    }
}