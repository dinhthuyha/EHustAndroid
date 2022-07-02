package com.prdcv.ehust.repo

import com.hadt.ehust.model.TopicStatus
import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class TopicRepository @Inject constructor(val eHustClient: EHustClient) {
    fun findTopicByIdTeacherAndIdProject(
        nameTeacher: String,
        idProject: String,
        idTeacher: Int
    ): Flow<State<List<Topic>>>{
        return object: NetworkBoundRepository<List<Topic>>(){
            override suspend fun fetchFromRemote(): Response<List<Topic>> {
                return eHustClient.findTopicByIdTeacherAndIdProject(nameTeacher, idProject, idTeacher)
            }
        }.asFlow()
    }

    fun updateTopicTable(
        idTopic: Int,
        status: TopicStatus,
        idStudent: Int
    ): Flow<State<ResponseBody>>{
        return object : NetworkBoundRepository<ResponseBody>(){
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.updateTopicTable(idTopic, status, idStudent)
            }
        }.asFlow()
    }

    fun submitTopicSuggestion(topic: Topic): Flow<State<ResponseBody>> {
        return object: NetworkBoundRepository<ResponseBody>() {
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.submitTopicSuggestion(topic)
            }
        }.asFlow()
    }
}