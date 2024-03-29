package com.prdcv.ehust.viewmodel

import android.content.SharedPreferences
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.model.User
import com.prdcv.ehust.repo.NewsRepository
import com.prdcv.ehust.repo.UserRepository
import com.prdcv.ehust.utils.SharedPreferencesKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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
    val newsRepository: NewsRepository
) : ViewModel() {
    private var _profileState = SingleLiveEvent<State<User>>()
    val profileState get() = _profileState

    private var _listUser = SingleLiveEvent<State<List<User>>>()
    val listUser get() = _listUser
    var loadingVisibility = ObservableInt()

    private var _newsState = SingleLiveEvent<State<List<News>>>()
    val newsState get() = _newsState

    var user: User? = null

    private var _token = SingleLiveEvent<State<Map<String,Any>>>()
    val token get() = _token

    private var _projectsState: MutableStateFlow<State<List<ClassStudent>>> = MutableStateFlow(State.Loading)
    val projectsState: StateFlow<State<List<ClassStudent>>> get() = _projectsState

    private var _schedulesState = SingleLiveEvent<State<List<ScheduleEvent>>>()
    val schedulesState get() = _schedulesState
    var schedules = listOf<ScheduleEvent>()
    fun login(id: Int, password: String) {
        viewModelScope.launch {
            userRepository.login(id, password).collect {
                _token.postValue(it)
            }
        }
    }

    fun decodeResponseLogin(hashMap: Map<String,Any>) {
        val token = hashMap["token"] as String
        //save to share preferences
        sharedPreferences.edit().putString(SharedPreferencesKey.TOKEN,token).commit()

        val profile = hashMap["profile"] as? Map<String,String>
        val id = (profile?.get("id") as String).toInt()
        val roleId = convertRole(profile?.get("role_id") as String)
        val fullName = profile?.get("full_name") as? String
        val grade = profile?.get("grade") as? String?: ""
        val ins = profile?.get("institute_of_management") as? String
        val gender = profile?.get("gender") as? String
        val course = profile?.get("course") as? String?: ""
        val email =  profile?.get("email") as? String ?: ""
        val cardeStatus =  profile?.get("cadre_status") as? String ?: ""
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

    fun getNews() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                newsRepository.getNews().collect {
                    _newsState.postValue(it)

                }
            }

        }
    }

    fun findAllProjectsById() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                userRepository.findAllProjectsByStudentId(user?.id!!).collect {
                    _projectsState.emit(it)
                }
            }

        }
    }
    fun findAllSchedules() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                userRepository.findAllSchedules(user?.id!!).collect {
                    _schedulesState.postValue(it)
                }
            }

        }
    }

    fun getScheduleToday(schedules: List<ScheduleEvent>): List<ScheduleEvent>{
        val today = LocalDate.now()
        val dateOfWeek = today?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

        return schedules.filter {
            val dateStudy = it.startDateStudy?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            dateStudy ==  dateOfWeek
        }
    }

}