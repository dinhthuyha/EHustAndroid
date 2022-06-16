package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.model.User
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

class TopicRepository @Inject constructor(val eHustClient: EHustClient) {
    fun findTopicByIdTeacherAndIdProject(
        idTeacher: Int,
        idProject: String
    ): Flow<State<List<Topic>>>{
        return object: NetworkBoundRepository<List<Topic>>(){
            override suspend fun fetchFromRemote(): Response<List<Topic>> {
                return eHustClient.findTopicByIdTeacherAndIdProject(idTeacher, idProject)
            }
        }.asFlow()
    }
}