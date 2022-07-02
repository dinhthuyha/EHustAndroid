package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName
import com.prdcv.ehust.viewmodel.TaskStatus
import java.time.LocalDate

data class TaskData(
    val id: Int = 23851,
    val title: String = "Tạo màn hình quản lý task",
    val status: TaskStatus = TaskStatus.FINISHED,
    val progress: Float = 0.8f,
    @SerializedName("due_date")
    val dueDate: LocalDate = LocalDate.now()
)