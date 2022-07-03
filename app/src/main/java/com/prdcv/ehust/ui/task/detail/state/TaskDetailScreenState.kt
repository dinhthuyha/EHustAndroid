package com.prdcv.ehust.ui.task.detail.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.util.Pair
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.model.TaskDetail
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class TaskDetailScreenState(
    var _taskDetail: TaskDetail = TaskDetail(),
    val isLoading: MutableState<Boolean> = mutableStateOf(false),
    val readOnly: MutableState<Boolean> = mutableStateOf(true),
    val taskTitle: MutableState<String> = mutableStateOf(""),
    val taskDescription: MutableState<String> = mutableStateOf(""),
    val taskStartDate: MutableState<LocalDate?> = mutableStateOf(null),
    val taskDueDate: MutableState<LocalDate?> = mutableStateOf(null),
    val taskEstimateTime: MutableState<String> = mutableStateOf(""),
    val taskSpendTime: MutableState<String> = mutableStateOf(""),
    val taskProgress: MutableState<String> = mutableStateOf(""),
    val taskAssignee: MutableState<String> = mutableStateOf(""),
    val taskComments: MutableState<List<Comment>> = mutableStateOf(emptyList()),
    val taskAttachments: SnapshotStateList<String> = mutableStateListOf(
        "kindpng_3651626.png",
        "kindpng_3651626.png",
        "kindpng_3651626.png",
        "kindpng_3651626.png",
        "kindpng_3651626.png"
    ),
    val uiDateRange: MutableState<String> = mutableStateOf("")
) {
    fun updateStates(taskDetail: TaskDetail) {
        _taskDetail = taskDetail
        taskTitle.value = taskDetail.title ?: ""
        taskDescription.value = taskDetail.description ?: ""
        taskStartDate.value = taskDetail.startDate
        taskDueDate.value = taskDetail.dueDate
        taskEstimateTime.value = taskDetail.estimateTime.toString()
        taskSpendTime.value = taskDetail.spendTime?.toString() ?: ""
        taskProgress.value = taskDetail.progress?.percent ?: ""
        taskAssignee.value = taskDetail.assignee ?: ""
        uiDateRange.value = selectedDatesFormatted
    }

    fun addFile(name: String) {
        taskAttachments.add(name)
    }

    val selectedDatesFormatted: String
        get() = "${taskStartDate.value?.format(SHORT_DATE_FORMAT)} - ${taskDueDate.value?.format(SHORT_DATE_FORMAT)}"

    fun getSelectedDates(): Pair<Long, Long>? {
        if (_taskDetail.startDate == null || _taskDetail.dueDate == null) return null
        return Pair(
            _taskDetail.startDate?.utcMilliseconds,
            _taskDetail.dueDate?.utcMilliseconds
        )
    }

    fun updateSelectedDates(selected: Pair<Long, Long>) {
        taskStartDate.value = Instant.ofEpochMilli(selected.first).atZone(ZoneOffset.UTC).toLocalDate()
        taskDueDate.value = Instant.ofEpochMilli(selected.second).atZone(ZoneOffset.UTC).toLocalDate()
        uiDateRange.value = selectedDatesFormatted
    }

    companion object {
        private val SHORT_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd")
    }
}

private val Float.percent: String
    get() = "${(this * 100).toInt()}"

private val LocalDate.utcMilliseconds: Long
    get() = atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

