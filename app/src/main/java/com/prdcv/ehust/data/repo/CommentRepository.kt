package com.prdcv.ehust.data.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.data.model.Comment
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class CommentRepository @Inject constructor(val eHustClient: EHustClient){

    fun findAllCommentByIdTask( idTask : Int): Flow<State<List<Comment>>>{
        return object:NetworkBoundRepository<List<Comment>>(){
            override suspend fun fetchFromRemote(): Response<List<Comment>> {
                return eHustClient.findAllCommentByIdTask(idTask)
            }
        }.asFlow()
    }

    fun postComment(idTask: Int, comment: Comment): Flow<State<Int>> {
        return object : NetworkBoundRepository<Int>(){
            override suspend fun fetchFromRemote(): Response<Int> {
                return eHustClient.postComment(idTask, comment)
            }
        }.asFlow()
    }

    fun deleteCommentById(id: Int): Flow<State<ResponseBody>>{
        return object : NetworkBoundRepository<ResponseBody>(){
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.deleteComment(id)
            }
        }.asFlow()
    }
}