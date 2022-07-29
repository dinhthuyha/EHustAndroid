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
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefreshState

import com.prdcv.ehust.common.State
import com.prdcv.ehust.data.model.DashBoard
import com.prdcv.ehust.data.model.PairingStudentWithTeacher
import com.prdcv.ehust.data.model.Role
import com.prdcv.ehust.data.model.Subject
import com.prdcv.ehust.data.model.User
import com.prdcv.ehust.data.repo.SubjectRepository
import com.prdcv.ehust.data.repo.UserRepository
import com.prdcv.ehust.ui.admin.HomeAdminFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AssignScreenState(
    val subjects: List<Subject> = emptyList(),
    val students: List<User> = emptyList(),
    val teachers: List<User> = emptyList(),
    val selectedSubject: Subject? = null,
    val submitButtonEnabled: Boolean = true,
    val informationDashBoard: MutableState<DashBoard> = mutableStateOf(DashBoard()),
    var teacherSelect: MutableState<String> = mutableStateOf(""),
    var studentSelect: MutableState<String> = mutableStateOf(""),
    var userSelect: MutableState<String> = mutableStateOf(""),
    var listFullNameTeacher: SnapshotStateList<String> = mutableStateListOf(),
    var listFullNameStudent: SnapshotStateList<String> = mutableStateListOf(),

    var listFullNameUser: SnapshotStateList<String> = mutableStateListOf(),
    var listProject: SnapshotStateList<String> = mutableStateListOf(),

    var predictionsTeacher: SnapshotStateList<String> = mutableStateListOf<String>(),
    var predictionsStudent: SnapshotStateList<String> = mutableStateListOf<String>(),
    var predictionsUser: SnapshotStateList<String> = mutableStateListOf<String>(),
    val listSemester: SnapshotStateList<Int> = mutableStateListOf(),
    val semesterStatus: MutableState<Int> = mutableStateOf(0),
    val listItemChecked: SnapshotStateList<PairingStudentWithTeacher> = mutableStateListOf(),
    val tableData: SnapshotStateList<PairingStudentWithTeacher> = mutableStateListOf(
        PairingStudentWithTeacher(
            1,
            1001,
            20173086,
            "DDo an",
            20212,
            "DDinh thuy ha",
            "Le ba vui",
            "",
            1
        )
    ),

    val refreshState: SwipeRefreshState = SwipeRefreshState(true),
    val user: MutableState<User> = mutableStateOf(User(id = 0, roleId = Role.ROLE_TEACHER)),
    val stateGetUser: MutableState<Boolean> = mutableStateOf(false)
) {

    fun isAllSelected(): Boolean {
        return selectedSubject != null && (studentSelect.value != "")
                && (teacherSelect.value != "")
    }

    fun getAllSemester(state: State<List<Int>>) {
        when (val _state = state) {
            is State.Success -> {
                listSemester.clear()
                listSemester.addAll(_state.data)
            }
            is State.Loading -> {
                refreshState.isRefreshing = true
            }
            is State.Error -> {
                // refreshState.isRefreshing = true
            }
        }
    }

    fun getInformationDashBoard(state: State<DashBoard>) {
        when (val _state = state) {
            is State.Success -> {
                informationDashBoard.value = _state.data
            }
            else -> {}
        }

    }

    fun getAllDataBySemester(state: State<List<PairingStudentWithTeacher>>) {
        when (val _state = state) {
            is State.Success -> {
                tableData.clear()
                tableData.addAll(_state.data)
                listFullNameUser.clear()
                val listFullNameTeacher = tableData.map { it.nameTeacher }.distinct()
                val listFullNameStudent = tableData.map { it.nameStudent }.distinct()
                val listNameProject = tableData.map { it.nameProject }.distinct()
                listFullNameUser.addAll(listFullNameStudent)
                listFullNameUser.addAll(listFullNameTeacher)
                listProject.addAll(listNameProject)
                refreshState.isRefreshing = false
            }
            is State.Loading -> {
                refreshState.isRefreshing = true
            }
            else -> {
                refreshState.isRefreshing = true
            }
        }
    }

    fun findByFullNameUser(value: State<User>, navController: NavController?) {
        when (val _state = value) {
            is State.Success -> {
                user.value = _state.data
                navController?.navigate(HomeAdminFragmentDirections.actionHomeAdminFragmentToProfileFragment(_state.data, true))
            }
            else -> {}
        }
    }


}


@HiltViewModel
class HomeAdminViewModel @Inject constructor(
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


    private fun findMaxSemester() {
        viewModelScope.launch {
            userRepository.findMaxSemester().collect {
                if (it is State.Success) {
                    val semester = mutableStateOf(it.data ?: 0)
                    uiState.copy(semesterStatus = semester)
                }
            }
        }
    }

    private fun getAllDataBySemester(semester: Int) {
        viewModelScope.launch {
            subjectRepository.getAllDataBySemester(semester).collect {
                if (it is State.Success)
                    delay(1000)
                uiState.getAllDataBySemester(it)
            }
        }
    }

    fun onSemesterSelected(semester: Int) {
        uiState.semesterStatus.value = semester
        getAllDataBySemester(uiState.semesterStatus.value)
    }

    private fun getAllUserInClass(nameCourse: String, role: Role) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getAllUserInProject(nameCourse, role).collect {
                when (val state = it) {
                    is State.Success -> {
                        uiState = when (role) {
                            Role.ROLE_TEACHER -> {
                                val list: SnapshotStateList<String> = mutableStateListOf()
                                list.addAll(state.data.map { it.fullName!! })
                                uiState.copy(teachers = state.data, listFullNameTeacher = list)
                            }
                            Role.ROLE_STUDENT -> {
                                val list: SnapshotStateList<String> = mutableStateListOf()
                                list.addAll(state.data.map { it.fullName!! })
                                uiState.copy(students = state.data, listFullNameStudent = list)
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
            uiState.copy(selectedSubject = project)
        getAllUserInClass(project.name, Role.ROLE_TEACHER)
        getAllUserInClass(project.name, Role.ROLE_STUDENT)
    }

    fun onSubmit() {
        Log.d("AssignVM", "onSubmit: clicked")
        if (uiState.isAllSelected()) {
            uiState = uiState.copy(submitButtonEnabled = false)
            val idStudent = uiState.students.first { it.fullName == uiState.studentSelect.value }.id
            val idTeacher = uiState.teachers.first { it.fullName == uiState.teacherSelect.value }.id
            assign(
                idStudent,
                idTeacher,
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
                        uiState = uiState.copy(
                            subjects = uiState.subjects,
                            teachers = emptyList(),
                            students = emptyList(),
                            selectedSubject = null,
                            teacherSelect = mutableStateOf(""),
                            studentSelect = mutableStateOf("")
                        )
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
        uiState.predictionsStudent.addAll(uiState.listFullNameStudent.filter { it.startsWith(value) })
    }

    fun getChangePredictionsTeacher(value: String) {
        uiState.predictionsTeacher.clear()

    }

    fun onItemChecked(t: PairingStudentWithTeacher) {
        uiState.listItemChecked.add(t)
    }

    fun onItemUnChecked(item: PairingStudentWithTeacher) {
        uiState.listItemChecked.remove(item)
    }

    private fun getAllSemester() {
        viewModelScope.launch {
            subjectRepository.getAllSemester().collect {
                uiState.getAllSemester(it)


            }
        }
    }

    fun fetchDataManagementScreen() {
        viewModelScope.launch {
            async { getAllSemester() }
            async { getAllDataBySemester(uiState.semesterStatus.value) }
        }
    }

    fun fetchDataAssignScreen() {
        viewModelScope.launch {
            async { getInformationDashBoard() }
            async { getAllProjectCurrentSemester() }

        }
    }

    fun deleteItemChecked() {
        if (uiState.listItemChecked.isEmpty()) return
        deleteAssigns(uiState.listItemChecked)
        uiState.listItemChecked.clear()
    }

    private fun deleteAssigns(list: List<PairingStudentWithTeacher>) {
        viewModelScope.launch {
            subjectRepository.deleteAssigns(list).collect {
                if (it is State.Success) {
                    getAllDataBySemester(uiState.semesterStatus.value)
                    snackbarHostState.showSnackbar("Xoá thành công")
                }
            }
        }
    }

    fun filterItem(selectedName: String) {
        val list =
            uiState.tableData.filter { it.nameStudent == selectedName || it.nameTeacher == selectedName || it.nameProject == selectedName }
        uiState.tableData.clear()
        uiState.tableData.addAll(list)
    }

    fun getProfileByUser(value: String, navController: NavController?) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                 findByFullNameUser(value, navController)
            }

        }
    }

    private fun findByFullNameUser(value: String,navController: NavController?){
        viewModelScope.launch {
            userRepository.searchAllUserByFullName(value).collect {
                uiState.findByFullNameUser(it, navController)
            }
        }
        
    }
}