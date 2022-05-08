package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class EhustRepository @Inject constructor(val eHustClient: EHustClient) {
    fun login(id:Int, password: String):Flow<State<String>>{
        return object : NetworkBoundRepository<String>() {
            override suspend fun fetchFromRemote(): Response<String> {
                return eHustClient.login(id, password)
            }
        }.asFlow()
    }
}