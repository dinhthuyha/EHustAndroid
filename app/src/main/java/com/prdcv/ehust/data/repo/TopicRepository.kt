package com.prdcv.ehust.data.repo

import com.hadt.ehust.model.TopicStatus
import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.data.model.MoreInformationTopic
import com.prdcv.ehust.data.model.Topic
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class TopicRepository @Inject constructor(val eHustClient: EHustClient) {
    fun findTopicByIdTeacherAndIdProject(
        nameTeacher: String,
        idProject: String,
        idTeacher: Int,
        semester : Int
    ): Flow<State<List<Topic>>>{
        return object: NetworkBoundRepository<List<Topic>>(){
            override suspend fun fetchFromRemote(): Response<List<Topic>> {
                return eHustClient.findTopicByIdTeacherAndIdProject(nameTeacher, idProject, idTeacher, semester)
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

    fun findByDetailTopic(idStudent: Int): Flow<State<MoreInformationTopic>> {
        return object: NetworkBoundRepository<MoreInformationTopic>() {
            override suspend fun fetchFromRemote(): Response<MoreInformationTopic> {
                return eHustClient.findByDetailTopic(idStudent)
            }
        }.asFlow()
    }

    fun updateStateProcessInformationTopic(topic: MoreInformationTopic): Flow<State<ResponseBody>>{
        return object : NetworkBoundRepository<ResponseBody>(){
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.updateStateProcessInformationTopic(topic)
            }
        }.asFlow()
    }
}