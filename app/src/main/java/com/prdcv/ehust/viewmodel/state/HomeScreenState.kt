package com.prdcv.ehust.viewmodel.state

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Meeting
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.model.TaskData
import com.prdcv.ehust.viewmodel.TaskStatus
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

data class HomeScreenState(
    private var taskList: List<TaskData> = emptyList(),
    val filteredTaskList: SnapshotStateList<TaskData> = mutableStateListOf(),
    val selectedTaskStatus: MutableState<TaskStatus> = mutableStateOf(TaskStatus.ALL),
    val schedulesState: SnapshotStateList<ScheduleEvent> = mutableStateListOf(),
    val meetings: SnapshotStateList<Meeting> = mutableStateListOf(),
    val meetingsToday: SnapshotStateList<Meeting> = mutableStateListOf(),
    val refreshState: SwipeRefreshState = SwipeRefreshState(true),
) {
    fun findAllSchedule(state: State<List<ScheduleEvent>>) {
        when (val _state = state) {
            is State.Success -> {
                Log.d("TAG", "findAllSchedule: ")
                schedulesState.clear()
                schedulesState.addAll(_state.data)
            }
            is State.Loading -> {
                refreshState.isRefreshing = true
            }
            is State.Error -> {
                refreshState.isRefreshing = false
            }
        }
    }

    private fun getMeetingToday(schedules: List<Meeting>): List<Meeting> {
        val today = LocalDate.now()
        val dateOfWeek = today?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

        return schedules.filter {
            val dateStudy =
                it.date?.dayOfWeek?.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            dateStudy == dateOfWeek
        }
    }
    fun addTasksFromState(state: State<List<TaskData>>) {
        when (val _state = state) {
            is State.Error -> {}
            State.Loading -> {
                refreshState.isRefreshing = true
            }
            is State.Success -> {
                taskList = _state.data.sortedByDescending(TaskData::id)
                filterTaskByStatus()
            }
        }
    }

    private fun filterTaskByStatus(status: TaskStatus? = null) {
        selectedTaskStatus.value = status?.let {
            if (selectedTaskStatus.value == it) TaskStatus.ALL else it
        } ?: TaskStatus.ALL

        filteredTaskList.clear()

        if (selectedTaskStatus.value == TaskStatus.ALL) {
            filteredTaskList.addAll(taskList)
            return
        }

        taskList
            .filter { selectedTaskStatus.value == it.status }
            .forEach { filteredTaskList.add(it) }
    }

    fun findAllMeeting(state: State<List<Meeting>>) {
        when (val _state = state) {
            is State.Success -> {
                Log.d("TAG", "findAllMeeting: ")
                meetings.clear()
                meetings.addAll(_state.data)
                meetingsToday.clear()
                meetingsToday.addAll(getMeetingToday(_state.data))
                refreshState.isRefreshing = false

            }
            is State.Loading -> {
                refreshState.isRefreshing = true

            }

            is State.Error -> {
                refreshState.isRefreshing = false
            }
        }
    }
}