package com.prdcv.ehust.ui.task.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import com.prdcv.ehust.R

@Composable
fun DatesUserInput(cationText: String,
                   datesSelected: String,
                   onDateSelectionClicked: () -> Unit,
                   readOnly: MutableState<Boolean>
) {
    if (!readOnly.value){
        CraneUserInput(
            onClick = onDateSelectionClicked,
            caption = if (datesSelected.isEmpty()) cationText else null,
            text = datesSelected,
            vectorImageId = R.drawable.ic_date
        )
    }else{
        CraneUserInput(
            caption = if (datesSelected.isEmpty()) cationText else null,
            text = datesSelected,
            vectorImageId = R.drawable.ic_date
        )
    }

}

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Calendar : Routes("calendar")
}

data class DateContentUpdates(
    val onDateSelectionClicked: () -> Unit,

    )