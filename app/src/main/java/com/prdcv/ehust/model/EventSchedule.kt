package com.prdcv.ehust.model

import java.time.LocalDate

data class EventSchedule(
    val id: String,
    val text: String,
    val date: LocalDate
)
