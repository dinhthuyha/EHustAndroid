package com.prdcv.ehust.model

import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.annotations.SerializedName
import com.hadt.ehust.model.StatusTask
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TaskDetail(
    val id: Int,
    val title: String,

    val status: StatusTask,
    val description: String? = null,
    @SerializedName("estimate_time")
    val estimateTime: Int? = null,

    @SerializedName("spend_time")
    val spendTime: Int? = null,

    @SerializedName("start_date")
    val startDate: LocalDate,

    @SerializedName("due_date")
    val dueDate: LocalDate,

    val progress: Float? = 0f,
    val assignee: String? = null,
) : Parcelable