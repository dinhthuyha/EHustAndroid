package com.prdcv.ehust.model

import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.annotations.SerializedName
import com.hadt.ehust.model.StatusTask
import com.prdcv.ehust.calendar.model.CalendarUiState
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class TaskDetail(
    val id: Int? = null,
    val title: String? = null,

    val status: StatusTask? = null,
    val description: String? = null,
    @SerializedName("estimate_time")
    val estimateTime: Int? = null,

    @SerializedName("spend_time")
    val spendTime: Int? = null,

    @SerializedName("start_date")
    val startDate: LocalDate? = null,

    @SerializedName("due_date")
    val dueDate: LocalDate? = null,

    val progress: Float? = 0f,
    val assignee: String? = null,
) {
    val selectedDatesFormatted: String
        get() {
            val output =
                "${startDate?.format(SHORT_DATE_FORMAT)} - ${dueDate?.format(SHORT_DATE_FORMAT)}"
            return output
        }

    companion object {
        private val SHORT_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd")
    }
}