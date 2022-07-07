package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.Attachment
import com.prdcv.ehust.model.AttachmentInfo
import com.prdcv.ehust.model.TaskDetail
import com.prdcv.ehust.network.EHustClient
import com.prdcv.ehust.model.TaskData
import io.minio.ObjectWriteResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
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

    fun updateTask(taskDetail: TaskDetail): Flow<State<ResponseBody>> {
        return object : NetworkBoundRepository<ResponseBody>() {
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.updateTask(taskDetail)
            }
        }.asFlow()
    }

    fun getAttachments(idTask: Int): Flow<State<List<Attachment>>> {
        return object : NetworkBoundRepository<List<Attachment>>() {
            override suspend fun fetchFromRemote(): Response<List<Attachment>> {
                return eHustClient.getAttachments(idTask)
            }
        }.asFlow()
    }

    fun addAttachment(idTask: Int, attachment: Attachment): Flow<State<List<Attachment>>> {
        return object : NetworkBoundRepository<List<Attachment>>() {
            override suspend fun fetchFromRemote(): Response<List<Attachment>> {
                return eHustClient.addAttachment(idTask, attachment)
            }
        }.asFlow()
    }

    fun uploadAttachment(attachmentInfo: AttachmentInfo): Flow<State<ObjectWriteResponse>> = flow {
        emit(State.Loading)
        val response = eHustClient.uploadAttachment(attachmentInfo)
        emit(State.Success(response))
    }.catch {
        emit(State.Error("attachment upload failed: ${it.message}"))
    }

    fun findAllTaskWillExpire(): Flow<State<List<TaskData>>> {
        return object : NetworkBoundRepository<List<TaskData>>() {
            override suspend fun fetchFromRemote(): Response<List<TaskData>> {
                return eHustClient.findAllTaskWillExpire()
            }
        }.asFlow()
    }

}