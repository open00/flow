package com.dflow.utils

sealed class UiResult<X : Any> {
    class Success<X: Any>(val data: X): UiResult<X>()
    class Error<X: Any>: UiResult<X>()
    class Loading<X: Any> : UiResult<X>()
}