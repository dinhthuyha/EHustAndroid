package com.prdcv.ehust.viewmodel.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.ClassStudent

data class ProjectsScreenState(
    val projects: SnapshotStateList<ClassStudent> = mutableStateListOf(),
    val refreshState: SwipeRefreshState = SwipeRefreshState(false),
    var maxSemester: Int = 0,
    val listSemester: SnapshotStateList<Int> = mutableStateListOf(),
    val semesterStatus: MutableState<Int?> = mutableStateOf(20212)
) {
    fun addProjectListFromState(state: State<List<ClassStudent>>) {
        when (val _state = state) {
            is State.Error -> refreshState.isRefreshing = false
            State.Loading -> refreshState.isRefreshing = true
            is State.Success -> {
                maxSemester = _state.data.maxOf { it.semester }
                projects.apply {
                    clear()
                    addAll(_state.data)
                }
                refreshState.isRefreshing = false
            }
        }
    }
}