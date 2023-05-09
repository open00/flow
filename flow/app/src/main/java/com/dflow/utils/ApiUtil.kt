package com.dflow.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response


sealed class ApiResult <out T> {
    data class Success<out R>(val data: R): ApiResult<R>()
    data class Error(val code: Int, val message: String?): ApiResult<Nothing>()
    object Loading: ApiResult<Nothing>()
}


suspend fun <T : Any> manageApi(execute: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        } else {
            ApiResult.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        ApiResult.Error(code = e.code(), message = e.message())
    }
}


fun <T> manageApiFlow(execute: suspend () -> Response<T>): Flow<ApiResult<T>> {
    return flow {
        emit(ApiResult.Loading)
        try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                emit(ApiResult.Success(body))
            }else{
                emit(ApiResult.Error(response.code(), response.message()))
            }
        } catch (e: HttpException) {
            emit(ApiResult.Error(e.code(), toString()))
        }
    }.flowOn(Dispatchers.IO)
}

