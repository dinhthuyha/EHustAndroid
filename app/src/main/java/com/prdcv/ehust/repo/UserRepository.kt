package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.DashBoard
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.model.User
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

class UserRepository @Inject constructor(val eHustClient: EHustClient) {
    fun login(id: Int, password: String): Flow<State<Map<String, Any>>> {
        return object : NetworkBoundRepository<Map<String, Any>>() {
            override suspend fun fetchFromRemote(): Response<Map<String, Any>> {
                return eHustClient.login(id, password)
            }
        }.asFlow()
    }

    fun getProfileById(id: Int): Flow<State<User>> {
        return object : NetworkBoundRepository<User>() {
            override suspend fun fetchFromRemote(): Response<User> {
                return eHustClient.getProfileById(id)
            }
        }.asFlow()
    }

    fun getListStudentInClass(grade: String): Flow<State<List<User>>> {
        return object : NetworkBoundRepository<List<User>>() {
            override suspend fun fetchFromRemote(): Response<List<User>> {
                return eHustClient.getListStudentInClass(grade)
            }
        }.asFlow()
    }

    fun findAllProjectsByStudentId(id: Int): Flow<State<List<ClassStudent>>> {
        return object : NetworkBoundRepository<List<ClassStudent>>() {
            override suspend fun fetchFromRemote(): Response<List<ClassStudent>> {
                return eHustClient.findAllProjectsByStudentId(id)
            }
        }.asFlow()
    }

    fun searchClassById(id: Int): Flow<State<ClassStudent>> {
        return object : NetworkBoundRepository<ClassStudent>() {
            override suspend fun fetchFromRemote(): Response<ClassStudent> {
                return eHustClient.searchClassById(id)
            }
        }.asFlow()
    }

    fun searchUserById(id: Int, role: Role): Flow<State<User>> {
        return object : NetworkBoundRepository<User>() {
            override suspend fun fetchFromRemote(): Response<User> {
                return eHustClient.searchUserById(id, role)
            }
        }.asFlow()
    }

    fun searchUserByFullName(fullName: String, role: Role): Flow<State<User>> {
        return object : NetworkBoundRepository<User>() {
            override suspend fun fetchFromRemote(): Response<User> {
                return eHustClient.searchUserByFullName(fullName, role)
            }
        }.asFlow()
    }
    fun findAllSchedules(id: Int): Flow<State<List<ScheduleEvent>>> {
        return object : NetworkBoundRepository<List<ScheduleEvent>>() {
            override suspend fun fetchFromRemote(): Response<List<ScheduleEvent>> {
                return eHustClient.findAllSchedules(id)
            }
        }.asFlow()
    }

    fun getAllUserInProject(nameCourse: String, role: Role): Flow<State<List<User>>> {
        return object : NetworkBoundRepository<List<User>>() {
            override suspend fun fetchFromRemote(): Response<List<User>> {
                return eHustClient.getAllUserInClass(nameCourse, role)
            }
        }.asFlow()
    }

    fun assignProjectInstructions(
        idStudent: Int,
        idTeacher: Int,
        nameProject: String
    ): Flow<State<ResponseBody>> {
        return object : NetworkBoundRepository<ResponseBody>() {
            override suspend fun fetchFromRemote(): Response<ResponseBody> {
                return eHustClient.assignProjectInstructions(idStudent, idTeacher, nameProject)
            }
        }.asFlow()
    }

    fun postMeeting(meeting: Meeting): Flow<State<List<Meeting>>>{
        return object : NetworkBoundRepository<List<Meeting>>(){
            override suspend fun fetchFromRemote(): Response<List<Meeting>> {
                return eHustClient.postMeeting(meeting)
            }
        }.asFlow()
    }

    fun findAllMeeting(idUserTeacher: Int, idUserStudent: Int): Flow<State<List<Meeting>>> {
        return object : NetworkBoundRepository<List<Meeting>>() {
            override suspend fun fetchFromRemote(): Response<List<Meeting>> {
                return eHustClient.findAllMeeting(idUserTeacher, idUserStudent)
            }
        }.asFlow()
    }

    fun findMaxSemester(): Flow<State<Int>> {
        return object : NetworkBoundRepository<Int>() {
            override suspend fun fetchFromRemote(): Response<Int> {
                return eHustClient.findMaxSemester()
            }
        }.asFlow()
    }

    fun getInformationDashBoard():Flow<State<DashBoard>>{
        return object : NetworkBoundRepository<DashBoard>(){
            override suspend fun fetchFromRemote(): Response<DashBoard> {
                return eHustClient.getInformationDashBoard()
            }
        }.asFlow()
    }

    fun checkToken(): Flow<State<Any>> {
        return object : NetworkBoundRepository<Any>() {
            override suspend fun fetchFromRemote(): Response<Any> {
                return eHustClient.checkToken()
            }
        }.asFlow()
    }

    fun searchAllUserByFullName(value: String): Flow<State<User>> {
        return object : NetworkBoundRepository<User>() {
            override suspend fun fetchFromRemote(): Response<User> {
                return eHustClient.searchAllUserByFullName(value)
            }
        }.asFlow()
    }
}