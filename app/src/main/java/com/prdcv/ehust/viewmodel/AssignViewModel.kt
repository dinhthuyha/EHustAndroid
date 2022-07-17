package com.prdcv.ehust.viewmodel

import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.DashBoard
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.Subject
import com.prdcv.ehust.model.User
import com.prdcv.ehust.repo.SubjectRepository
import com.prdcv.ehust.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssignScreenState(
    val subjects: List<Subject> = emptyList(),
    val students: List<User> = emptyList(),
    val teachers: List<User> = emptyList(),
    val selectedSubject: Subject? = null,
    val selectedStudent: User? = null,
    val selectedTeacher: User? = null,
    val submitButtonEnabled: Boolean = true,
    val informationDashBoard: MutableState<DashBoard> = mutableStateOf(DashBoard()),
    var streetAddress: MutableState<String> = mutableStateOf("hadinh")
) {
    fun isAllSelected(): Boolean {
        return selectedSubject
            ?.let { selectedStudent }
            ?.let { selectedTeacher }
            ?.let { true } ?: false
    }

    fun getInformationDashBoard(state: State<DashBoard>) {
        when (val _state = state) {
            is State.Success -> {
                informationDashBoard.value = _state.data
            }
            else -> {}
        }

    }
}

@HiltViewModel
class AssignViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    val snackbarHostState: SnackbarHostState = SnackbarHostState()
    var uiState by mutableStateOf(AssignScreenState())
        private set

    /**
     * admin get ds project cua toan truong
     */
    fun getAllProjectCurrentSemester() {
        viewModelScope.launch(Dispatchers.IO) {
            subjectRepository.getAllProjectCurrentSemester().collect {
                when (val state = it) {
                    is State.Success -> {
                        uiState = uiState.copy(subjects = state.data)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getAllUserInClass(nameCourse: String, role: Role) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getAllUserInProject(nameCourse, role).collect {
                when (val state = it) {
                    is State.Success -> {
                        uiState = when (role) {
                            Role.ROLE_TEACHER -> uiState.copy(teachers = state.data)
                            Role.ROLE_STUDENT -> uiState.copy(students = state.data)
                            else -> uiState
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun onProjectSelected(project: Subject) {
        uiState =
            uiState.copy(selectedSubject = project, selectedStudent = null, selectedTeacher = null)
        getAllUserInClass(project.name, Role.ROLE_TEACHER)
        getAllUserInClass(project.name, Role.ROLE_STUDENT)
    }

    fun onTeacherSelected(user: User) {
        uiState = uiState.copy(selectedTeacher = user)
    }

    fun onStudentSelected(user: User) {
        uiState = uiState.copy(selectedStudent = user)
    }

    fun onSubmit() {
        Log.d("AssignVM", "onSubmit: clicked")
        if (uiState.isAllSelected()) {
            uiState = uiState.copy(submitButtonEnabled = false)
            assign(
                uiState.selectedStudent!!.id,
                uiState.selectedTeacher!!.id,
                uiState.selectedSubject!!.name
            )
        }
    }

    private fun assign(
        idStudent: Int,
        idTeacher: Int,
        nameProject: String
    ) {
        viewModelScope.launch {
            userRepository.assignProjectInstructions(idStudent, idTeacher, nameProject).collect {
                when (val state = it) {
                    is State.Error -> snackbarHostState.showSnackbar("Error: ${state.exception}")
                    is State.Success -> {
                        snackbarHostState.showSnackbar("Success")
                        uiState = AssignScreenState(subjects = uiState.subjects)
                    }
                    else -> return@collect
                }
                uiState = uiState.copy(submitButtonEnabled = true)
            }
        }
    }


    fun getInformationDashBoard() {
        viewModelScope.launch {
            userRepository.getInformationDashBoard().collect {
                uiState.getInformationDashBoard(it)
            }
        }
    }
}