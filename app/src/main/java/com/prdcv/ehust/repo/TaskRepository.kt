package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.TaskDetail
import com.prdcv.ehust.network.EHustClient
import com.prdcv.ehust.model.TaskData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class TaskRepository @Inject constructor(
    val eHustClient: EHustClient
) {

    fun findAllTaskByIdTopic(idTopic: Int): Flow<State<List<TaskData>>> {
        return object : NetworkBoundRepository<List<TaskData>>() {
            override suspend fun fetchFromRemote(): Response<List<TaskData>> {
                return eHustClient.findAllTaskByIdTopic(idTopic)
            }
        }.asFlow()
    }

    fun getDetailTask(id: Int): Flow<State<TaskDetail>> {
        return object : NetworkBoundRepository<TaskDetail>() {
            override suspend fun fetchFromRemote(): Response<TaskDetail> {
                return eHustClient.getDetailTask(id)
            }
        }.asFlow()
    }
}