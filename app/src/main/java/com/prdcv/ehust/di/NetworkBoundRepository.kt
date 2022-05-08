package com.prdcv.ehust.di

import com.prdcv.ehust.common.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * A repository which provides resource from local database as well as remote end point.
 * [REQUEST] represents the type for network.
 */
abstract class NetworkBoundRepository<REQUEST> {

    fun asFlow() = flow<State<REQUEST>> {
        emit(State.Loading)
        val apiResponse = fetchFromRemote()
        val remotePosts = apiResponse.body()
        if (!apiResponse.isSuccessful || remotePosts == null) {
            emit(State.Error("${apiResponse.code()}, ${apiResponse.errorBody()}"))
        }else{
            emit(State.Success(remotePosts))
        }
    }.catch { e ->
        // Exception occurred! Emit error
        emit(State.Error(e.message ?: ""))
    }

    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>

}