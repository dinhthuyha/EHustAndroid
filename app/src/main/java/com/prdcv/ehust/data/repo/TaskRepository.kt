package com.prdcv.ehust.data.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.data.model.Attachment
import com.prdcv.ehust.data.model.AttachmentInfo
import com.prdcv.ehust.data.model.News
import com.prdcv.ehust.data.model.TaskDetail
import com.prdcv.ehust.network.EHustClient
import com.prdcv.ehust.data.model.TaskData
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

    fun newTask(idTopic: Int, task: TaskDetail): Flow<State<Int>> {
        return object : NetworkBoundRepository<Int>() {
            override suspend fun fetchFromRemote(): Response<Int> {
                return eHustClient.newTask(idTopic, task)
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

    fun addAttachment(idComment: Int, attachment: Attachment): Flow<State<Any>> {
        return object : NetworkBoundRepository<Any>() {
            override suspend fun fetchFromRemote(): Response<Any> {
                return eHustClient.addAttachment(idComment, attachment)
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

    fun updateNotificationNewTask(notification: News): Flow<State<ResponseBody>> {
        return object : NetworkBoundRepository<ResponseBody>() {
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.updateNotificationNewTask(notification)
            }
        }.asFlow()
    }

    fun notificationUpdateTask(taskDetail: TaskDetail): Flow<State<ResponseBody>> {
        return object : NetworkBoundRepository<ResponseBody>() {
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.notificationUpdateTask(taskDetail)
            }
        }.asFlow()
    }

    fun downloadFile(url: String): Flow<State<ResponseBody>>{
        return object : NetworkBoundRepository<ResponseBody>(){
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.downloadFile(url)
            }
        }.asFlow()
    }
}