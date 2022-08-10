package com.prdcv.ehust.data.model

import com.google.gson.annotations.SerializedName
import com.prdcv.ehust.viewmodel.TaskStatus
import java.time.LocalDate


data class TaskDetail(
    val id: Int? = null,
    val title: String? = null,

    val status: TaskStatus? = null,
    val description: String? = null,
    @SerializedName("estimate_time")
    val estimateTime: Int? = null,

//    @SerializedName("spend_time")
//    val spendTime: Int? = null,

    @SerializedName("start_date")
    val startDate: LocalDate? = null,

    @SerializedName("due_date")
    val dueDate: LocalDate? = null,

    val progress: Float? = 0f,
    val assignee: String? = null,
)