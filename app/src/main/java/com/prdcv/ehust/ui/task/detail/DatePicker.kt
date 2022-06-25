package com.prdcv.ehust.ui.task.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.prdcv.ehust.R

@Composable
fun DatesUserInput(datesSelected: String, onDateSelectionClicked: () -> Unit) {
    CraneUserInput(
        onClick = onDateSelectionClicked,
        caption = if (datesSelected.isEmpty()) stringResource(R.string.select_dates) else null,
        text = datesSelected,
        vectorImageId = R.drawable.ic_date
    )
}

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Calendar : Routes("calendar")
}

data class DateContentUpdates(
    val onDateSelectionClicked: () -> Unit,

    )