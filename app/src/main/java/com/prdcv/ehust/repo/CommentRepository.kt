package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.Comment
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

    fun postComment(comment: Comment): Flow<State<ResponseBody>>{
        return object : NetworkBoundRepository<ResponseBody>(){
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.postComment(comment)
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