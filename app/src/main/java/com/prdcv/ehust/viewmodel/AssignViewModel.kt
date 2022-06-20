package com.prdcv.ehust.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.Subject
import com.prdcv.ehust.model.User
import com.prdcv.ehust.repo.SubjectRepository
import com.prdcv.ehust.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class AssignViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {


    private var _projectsState: MutableStateFlow<State<List<Subject>>> =
        MutableStateFlow(State.Loading)
    val projectsState: StateFlow<State<List<Subject>>> get() = _projectsState

    private var _studentsState: MutableStateFlow<State<List<User>>> =
        MutableStateFlow(State.Loading)
    val studentsState: StateFlow<State<List<User>>> get() = _studentsState

    private var _teachersState: MutableStateFlow<State<List<User>>> =
        MutableStateFlow(State.Loading)
    val teachersState: StateFlow<State<List<User>>> get() = _teachersState

    private var _assignState: MutableStateFlow<State<ResponseBody>> =
        MutableStateFlow(State.Loading)
    val assignState: StateFlow<State<ResponseBody>> get() = _assignState


    /**
     * admin get ds project cua toan truong
     */
    fun getAllProjectCurrentSemester() {
        viewModelScope.launch(Dispatchers.IO) {
            subjectRepository.getAllProjectCurrentSemester().collect {
                _projectsState.emit(it)
            }
        }
    }

    fun getAllUserInClass(nameCourse: String, role: Role) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getAllUserInProject(nameCourse, role).collect {
                when(role) {
                    Role.ROLE_TEACHER -> _teachersState.emit(it)
                    Role.ROLE_STUDENT -> _studentsState.emit(it)
                    else -> {}
                }
            }
        }
    }

    fun assign(
        idStudent: Int,
        idTeacher: Int,
        nameProject: String
    ) {
        viewModelScope.launch {
            userRepository.assignProjectInstructions(idStudent, idTeacher, nameProject).collect{
                _assignState.emit(it)
            }
        }
    }

}