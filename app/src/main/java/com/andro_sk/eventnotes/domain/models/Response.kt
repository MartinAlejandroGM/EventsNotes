package com.andro_sk.eventnotes.domain.models

sealed class Response<out T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val error: Throwable) : Response<Nothing>()
}