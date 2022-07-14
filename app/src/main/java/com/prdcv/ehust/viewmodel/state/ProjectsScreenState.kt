package com.prdcv.ehust.viewmodel.state

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.ProjectTeacher

data class ProjectsScreenState(
    val projects: SnapshotStateList<ClassStudent> = mutableStateListOf(),
    val refreshState: SwipeRefreshState = SwipeRefreshState(false),
    val listSemester: SnapshotStateList<Int> = mutableStateListOf(),
    val semesterStatus: MutableState<Int?> = mutableStateOf(0),
    val listProject: SnapshotStateList<ProjectTeacher> = mutableStateListOf()
) {
    fun addProjectListFromState(state: State<List<ClassStudent>>) {
        when (val _state = state) {
            is State.Error -> refreshState.isRefreshing = true
            State.Loading -> refreshState.isRefreshing = false
            is State.Success -> {
                projects.apply {
                    clear()
                    addAll(_state.data)
                }
            }
        }
    }

    fun getAllProjectByIdTeacherAndSemester(state: State<List<ProjectTeacher>>) {
        when (val _state = state) {
            is State.Success -> {
                val list = mutableListOf<ProjectTeacher>()
                list.addAll(_state.data)
                list.forEach { t ->
                   val numberStudentInProject = list.filter { it.codeProject ==t.codeProject }.size
                    t.numberOfStudentsGuiding = numberStudentInProject
                }
                listProject.apply {
                    clear()
                    addAll(list.distinctBy { it.codeProject })
                }
            }
            is State.Loading -> {
                refreshState.isRefreshing = false
            }
            is State.Error -> {
                refreshState.isRefreshing = true
            }
        }
    }

    fun getAllSemester(state: State<List<Int>>) {
        when (val _state = state) {
            is State.Success -> {
                listSemester.clear()
                listSemester.addAll(_state.data)
            }
            is State.Loading -> {
                refreshState.isRefreshing = false
            }
            is State.Error -> {
                refreshState.isRefreshing = true
            }
        }
    }
}