package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.Subject
import com.prdcv.ehust.network.EHustClient

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    val eHustClient: EHustClient
    ) {

    fun getAllProjectCurrentSemester(): Flow<State<List<Subject>>>{
        return object: NetworkBoundRepository<List<Subject>>(){
            override suspend fun fetchFromRemote(): Response<List<Subject>> {
                return eHustClient.getAllProjectCurrentSemester()
            }
        }.asFlow()
   }
}