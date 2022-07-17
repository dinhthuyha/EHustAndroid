package com.prdcv.ehust.viewmodel

import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
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
    var teacherSelect: MutableState<String> = mutableStateOf(""),
    var studentSelect: MutableState<String> = mutableStateOf(""),
    var listTeacher: SnapshotStateList<String> = mutableStateListOf<String>(
        "hà nội",
        "thành phố hồ chí minh",
        "nha trang",
        " vũng tàu",
        "thanh ho",
        "ha thanh",
        "ha giang"
    ),
    var listStudent: SnapshotStateList<String> = mutableStateListOf<String>(
        "hà nội",
        "thành phố hồ chí minh",
        "nha trang",
        " vũng tàu",
        "thanh ho",
        "ha thanh",
        "ha giang"
    ),
    var predictionsTeacher: SnapshotStateList<String> = mutableStateListOf<String>(),
    var predictionsStudent: SnapshotStateList<String> = mutableStateListOf<String>()
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
                            Role.ROLE_TEACHER -> {
                                val list : SnapshotStateList<String> = mutableStateListOf()
                                list.addAll(state.data.map { it.fullName!! })
                                uiState.copy(teachers = state.data, listTeacher = list)
                            }
                            Role.ROLE_STUDENT -> {
                                val list : SnapshotStateList<String> = mutableStateListOf()
                                list.addAll(state.data.map { it.fullName!! })
                                uiState.copy(students = state.data, listStudent = list)
                            }
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

    fun onItemStudentSelect(selectedPlaceItem: String) {
        viewModelScope.launch {
            uiState.studentSelect.value = selectedPlaceItem
            uiState.predictionsStudent.clear()
        }


    }

    fun onAutoCompleteClearStudent(predictions: MutableState<String>) {
        viewModelScope.launch {
            clearPredictions(predictions)
        }
    }

    private fun clearPredictions(predictions: MutableState<String>) {
        predictions.value = ""
    }

    fun onItemTeacherSelect(selectedItem: String) {
        viewModelScope.launch {
            uiState.teacherSelect.value = selectedItem
            uiState.predictionsTeacher.clear()
        }
    }

    fun onAutoCompleteClearTeacher(teacherSelect: MutableState<String>) {
        viewModelScope.launch {
            clearPredictions(teacherSelect)
        }
    }

    fun getChangePredictionsStudent(value: String) {
        uiState.predictionsStudent.clear()
        uiState.predictionsStudent.addAll(uiState.listStudent.filter { it.startsWith(value) })
    }

    fun getChangePredictionsTeacher(value: String) {
        uiState.predictionsTeacher.clear()
        uiState.predictionsTeacher.addAll(uiState.listTeacher.filter { it.startsWith(value) })

    }
}