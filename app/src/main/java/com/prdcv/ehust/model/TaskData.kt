package com.prdcv.ehust.model

import com.prdcv.ehust.viewmodel.TaskStatus
import java.time.LocalDate

data class TaskData(
    val id: Int = 23851,
    val title: String = "Tạo màn hình quản lý task",
    val status: TaskStatus = TaskStatus.FINISHED,
    val progress: Float = 0.8f,
    val dueDate: LocalDate = LocalDate.now()
)