package com.prdcv.ehust.ui.task.detail.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.util.Pair
import com.prdcv.ehust.model.Attachment
import com.prdcv.ehust.model.AttachmentInfo
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.model.TaskDetail
import com.prdcv.ehust.viewmodel.TaskStatus
import java.time.Instant
import java.time.LocalDate
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
    val taskAttachments: MutableState<List<Attachment>> = mutableStateOf(emptyList()),
    val uiDateRange: MutableState<String> = mutableStateOf(""),
    val progressBarVisible: MutableState<Boolean> = mutableStateOf(false),
    val uploadProgress: MutableState<Float> = mutableStateOf(0f),
    val fileToUpload: MutableState<AttachmentInfo?> = mutableStateOf(null),
    val taskStatus: MutableState<TaskStatus?> = mutableStateOf(null),
    val listStatusTask: List<TaskStatus> = listOf(TaskStatus.NEW,TaskStatus.IN_PROGRESS, TaskStatus.FINISHED, TaskStatus.CANCELED),
) {
    fun updateStates(taskDetail: TaskDetail) {
        _taskDetail = taskDetail
        taskTitle.value = taskDetail.title ?: ""
        taskStatus.value = taskDetail.status
        taskDescription.value = taskDetail.description ?: ""
        taskStartDate.value = taskDetail.startDate
        taskDueDate.value = taskDetail.dueDate
        taskEstimateTime.value = taskDetail.estimateTime.toString()
        taskSpendTime.value = taskDetail.spendTime?.toString() ?: ""
        taskProgress.value = taskDetail.progress?.percent ?: ""
        taskAssignee.value = taskDetail.assignee ?: ""
        uiDateRange.value = selectedDatesFormatted
    }

    private val selectedDatesFormatted: String
        get() = "${taskStartDate.value?.format(SHORT_DATE_FORMAT)} - ${
            taskDueDate.value?.format(
                SHORT_DATE_FORMAT
            )
        }"

    fun getSelectedDates(): Pair<Long, Long>? {
        if (_taskDetail.startDate == null || _taskDetail.dueDate == null) return null
        return Pair(
            _taskDetail.startDate?.utcMilliseconds,
            _taskDetail.dueDate?.utcMilliseconds
        )
    }

    fun updateSelectedDates(selected: Pair<Long, Long>) {
        taskStartDate.value =
            Instant.ofEpochMilli(selected.first).atZone(ZoneOffset.UTC).toLocalDate()
        taskDueDate.value =
            Instant.ofEpochMilli(selected.second).atZone(ZoneOffset.UTC).toLocalDate()
        uiDateRange.value = selectedDatesFormatted
    }

    fun updateUploadProgress(p: Float) {
        uploadProgress.value = p
    }

    val newTaskDetail: TaskDetail
        get() {
            val id = _taskDetail.id
            val title = taskTitle.value.takeIf { it.isNotBlank() }
            val status = taskStatus.value
            val des = taskDescription.value.takeIf { it.isNotBlank() }
            val startDate = taskStartDate.value
            val dueDate = taskDueDate.value
            val estimateTime = taskEstimateTime.value.takeIf { it.isNotBlank() }?.toInt()
            val spendTime = taskSpendTime.value.takeIf { it.isNotBlank() }?.toInt()
            val progress = taskProgress.value.takeIf { it.isNotBlank() }?.toFloat()?.div(100f)
            val assignee = taskAssignee.value.takeIf { it.isNotBlank() }

            return TaskDetail(
                id = id,
                title = title,
                status = status,
                description = des,
                spendTime = spendTime,
                estimateTime = estimateTime,
                progress = progress,
                assignee = assignee,
                startDate = startDate,
                dueDate = dueDate
            )
        }

    companion object {
        private val SHORT_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd")
    }
}

private val Float.percent: String
    get() = "${(this * 100).toInt()}"

private val LocalDate.utcMilliseconds: Long
    get() = atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

