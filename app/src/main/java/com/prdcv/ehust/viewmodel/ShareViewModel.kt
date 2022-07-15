package com.prdcv.ehust.viewmodel

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.databinding.ObservableInt
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.model.StatusNotification
import com.hadt.ehust.model.TypeNotification
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.model.User
import com.prdcv.ehust.repo.NewsRepository
import com.prdcv.ehust.repo.TaskRepository
import com.prdcv.ehust.repo.UserRepository
import com.prdcv.ehust.utils.SharedPreferencesKey
import com.prdcv.ehust.viewmodel.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,
    private val newsRepository: NewsRepository,
    private val taskRepository: TaskRepository
) : BaseViewModel() {
    private var _profileState = SingleLiveEvent<State<User>>()
    val profileState get() = _profileState

    private var _listUser = SingleLiveEvent<State<List<User>>>()
    val listUser get() = _listUser
    var loadingVisibility = ObservableInt()

    private var _newsState = MutableStateFlow<State<List<News>>>(State.Loading)
    val newsState: StateFlow<State<List<News>>> get() = _newsState

    private var _token = SingleLiveEvent<State<Map<String, Any>>>()
    val token get() = _token
    var maxSemester: Int? = 0

    var uiState by mutableStateOf(HomeScreenState())
        private set

    fun login(id: Int, password: String) {
        viewModelScope.launch {
            uiState = HomeScreenState()
            userRepository.login(id, password).collect {
                _token.postValue(it)
            }
        }
    }

    fun decodeResponseLogin(hashMap: Map<String, Any>) {
        val token = hashMap["token"] as String
        //save to share preferences
        sharedPreferences.edit().putString(SharedPreferencesKey.TOKEN, token).commit()

        val profile = hashMap["profile"] as? Map<String, String>
        val id = (profile?.get("id") as String).toInt()
        val roleId = convertRole(profile?.get("role_id") as String)
        val fullName = profile?.get("full_name") as? String
        val grade = profile?.get("grade") as? String ?: ""
        val ins = profile?.get("institute_of_management") as? String
        val gender = profile?.get("gender") as? String
        val course = profile?.get("course") as? String ?: ""
        val email = profile?.get("email") as? String ?: ""
        val cardeStatus = profile?.get("cadre_status") as? String ?: ""
        val unit = profile?.get("unit") as? String ?: ""
        val imageBg = profile?.get("image_background") as? String
        val imageAva = profile?.get("image_avatar") as? String

        user = User(
            id,
            fullName,
            ins,
            gender,
            grade,
            course,
            email,
            cardeStatus,
            unit,
            roleId,
            imageBg,
            imageAva
        )
    }

    private fun convertRole(roleId: String): Role {
        return when (roleId) {
            Role.ROLE_ADMIN.name -> Role.ROLE_ADMIN
            Role.ROLE_STUDENT.name -> Role.ROLE_STUDENT
            Role.ROLE_TEACHER.name -> Role.ROLE_TEACHER
            else -> Role.ROLE_UNKNOWN
        }
    }

    fun getListStudentInClass() {
        viewModelScope.launch {
            userRepository.getListStudentInClass(user?.grade!!).collect {
                _listUser.postValue(it)
                if (it is State.Success) {
                    loadingVisibility.set(View.GONE)
                }
            }
        }
    }

    fun getNews(type: TypeNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.getNews(type).collect {
                _newsState.emit(it)
            }
        }
    }

    fun updateStatusNew(
        id: Int,
        type: TypeNotification,
        status: StatusNotification
    ){
        viewModelScope.launch { newsRepository.updateStatusNew(id, type, status).collect{
            _newsState.emit(it)
        } }
    }
    fun findAllSchedules() {
        viewModelScope.launch {
                userRepository.findAllSchedules(user?.id!!).collect {
                    uiState.findAllSchedule(it)
                }
        }
    }

    fun findMaxSemester() {
        viewModelScope.launch {
            userRepository.findMaxSemester().collect{
                if (it is State.Success){
                    Log.d("TAG", "findMaxSemester: ${maxSemester}")
                    maxSemester = it.data
                }
            }
        }
    }

    fun getScheduleToday(schedules: List<ScheduleEvent>): List<ScheduleEvent> {
        val today = LocalDate.now()
        val dateOfWeek = today?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

        return schedules.filter {
            val dateStudy =
                it.startDateStudy?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            dateStudy == dateOfWeek
        }
    }

    fun postMeeting(meeting: Meeting) {
        viewModelScope.launch {
            userRepository.postMeeting(meeting).collect {
                findAllMeeting()
            }
        }

    }

    fun fetchDataHomeScreen() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                async { findAllSchedules()  }
                async { findAllTaskWillExpire() }
                async { findMaxSemester() }
                async { findAllMeeting() }
            }


        }
    }

    fun findAllMeeting() {
        var idUserTeacher: Int = 0
        var idUserStudent: Int = 0
        viewModelScope.launch {
            when (user?.roleId) {
                Role.ROLE_TEACHER -> {
                    idUserTeacher = user?.id!!
                }
                Role.ROLE_STUDENT -> {
                    idUserStudent = user?.id!!
                }
                else -> {}
            }
            userRepository.findAllMeeting(idUserTeacher, idUserStudent).collect {
                if (it is State.Success)
                    delay(2000)
                uiState.findAllMeeting(it)
            }
        }
    }

    fun findAllTaskWillExpire() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.findAllTaskWillExpire().collect { uiState.addTasksFromState(it) }
        }
    }

}