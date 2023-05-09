package com.dflow.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException

private const val RETRY_TIME_IN_MILLIS = 15_000L
private const val RETRY_ATTEMPT_COUNT = 3

sealed interface ResultFlow<out T> {
    data class Success<T>(val data: T) : ResultFlow<T>
    data class Error(val exception: Throwable? = null) : ResultFlow<Nothing>
    object Loading : ResultFlow<Nothing>
}


fun <T> Flow<T>.asResult(): Flow<ResultFlow<T>> {
    return this
        .map<T, ResultFlow<T>> {
            ResultFlow.Success(it)
        }
        .onStart { emit(ResultFlow.Loading) }
        .retryWhen { cause, attempt ->
            if (cause is IOException && attempt < RETRY_ATTEMPT_COUNT) {
                delay(RETRY_TIME_IN_MILLIS)
                true
            } else {
                false
            }
        }
        .catch { emit(ResultFlow.Error(it)) }
}