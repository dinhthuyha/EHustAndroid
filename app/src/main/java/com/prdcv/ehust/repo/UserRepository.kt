package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.User
import com.prdcv.ehust.network.EHustClient
import com.prdcv.ehust.ui.search.ItemSearch
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(val eHustClient: EHustClient) {
    fun login(id:Int, password: String):Flow<State<String>>{
        return object : NetworkBoundRepository<String>() {
            override suspend fun fetchFromRemote(): Response<String> {
                return eHustClient.login(id, password)
            }
        }.asFlow()
    }

    fun getProfileById(id: Int): Flow<State<User>>{
        return object : NetworkBoundRepository<User>(){
            override suspend fun fetchFromRemote(): Response<User> {
                return eHustClient.getProfileById(id)
            }
        }.asFlow()
    }

    fun getListStudentInClass( grade: String): Flow<State<List<User>>>{
        return object: NetworkBoundRepository<List<User>>(){
            override suspend fun fetchFromRemote(): Response<List<User>> {
                return eHustClient.getListStudentInClass(grade)
            }
        }.asFlow()
    }

    fun findAllProjectsByStudentId( id: Int ): Flow<State<List<ClassStudent>>>{
        return object: NetworkBoundRepository<List<ClassStudent>>(){
            override suspend fun fetchFromRemote(): Response<List<ClassStudent>> {
                return eHustClient.findAllProjectsByStudentId(id)
            }
        }.asFlow()
    }

    fun searchClassById(id: Int): Flow<State<ClassStudent>>{
        return object : NetworkBoundRepository<ClassStudent>(){
            override suspend fun fetchFromRemote(): Response<ClassStudent> {
                return eHustClient.searchClassById(id)
            }
        }.asFlow()
    }
     fun searchUserById(id: Int): Flow<State<User>>{
        return object : NetworkBoundRepository<User>(){
            override suspend fun fetchFromRemote(): Response<User> {
                return eHustClient.searchUserById(id)
            }
        }.asFlow()
    }

}