package com.prdcv.ehust.common

sealed class State<out R> {
    data class Success<out T>(val data: T) : State<T>()
    data class Error<out T>(val exception: String) : State<T>()
    object Loading : State<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [State] is of type [Success] & holds non-null [Success.data].
 */
val State<*>.succeeded
    get() = this is State.Success && data != null