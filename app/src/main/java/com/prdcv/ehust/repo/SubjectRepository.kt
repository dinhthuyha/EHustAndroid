package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.PairingStudentWithTeacher
import com.prdcv.ehust.model.Subject
import com.prdcv.ehust.network.EHustClient

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Path
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

    fun getAllSemester(): Flow<State<List<Int>>>{
        return object : NetworkBoundRepository<List<Int>>(){
            override suspend fun fetchFromRemote(): Response<List<Int>> {
                return eHustClient.getAllSemester()
            }
        }.asFlow()
    }

    fun getAllProjectByIdTeacherAndSemester(idTeacher: Int, semester: Int): Flow<State< List<PairingStudentWithTeacher>>>{
        return object : NetworkBoundRepository< List<PairingStudentWithTeacher>>(){
            override suspend fun fetchFromRemote(): Response<List<PairingStudentWithTeacher>> {
                return eHustClient.getAllProjectByIdTeacherAndSemester(idTeacher, semester)
            }
        }.asFlow()
    }

    fun getAllDataBySemester( semester: Int): Flow<State< List<PairingStudentWithTeacher>>>{
        return object : NetworkBoundRepository< List<PairingStudentWithTeacher>>(){
            override suspend fun fetchFromRemote(): Response<List<PairingStudentWithTeacher>> {
                return eHustClient.getAllDataBySemester(semester)
            }
        }.asFlow()
    }

    fun deleteAssigns(list: List<PairingStudentWithTeacher>): Flow<State<ResponseBody>>{
        return object : NetworkBoundRepository< ResponseBody>(){
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.deleteAssigns(list)
            }
        }.asFlow()
    }


}