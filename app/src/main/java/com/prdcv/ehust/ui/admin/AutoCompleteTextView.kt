package com.prdcv.ehust.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prdcv.ehust.viewmodel.AssignViewModel
import kotlinx.coroutines.launch

@Composable
fun <T> QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit,
    predictions: SnapshotStateList<T>,
    list: SnapshotStateList<T>
) {
    var showClearButton by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
                if (query == "" && showClearButton) {
                    predictions.addAll(list)
                }
            },
        value = query,
        onValueChange = onQueryChanged,
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        trailingIcon = {
            if (showClearButton) {
                IconButton(onClick = { onClearClick() }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                }
            }

        },
        keyboardActions = KeyboardActions(onDone = {
            onDoneActionClick()
        }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )


}

@Composable
fun RowComplete(viewModel: AssignViewModel, title: String,
                selected: MutableState<String>,
                predictionsUser: SnapshotStateList<String>,
                listUser: SnapshotStateList<String>,
                hideKeyboard: ()-> Unit) {
    val scope = rememberCoroutineScope()
    RowAutoCompleteTextView(title = title,selected, predictionsUser, listUser ) { action ->

        when (action) {
            is UserUIAction.OnAddressSelected -> {
                scope.launch {
                 hideKeyboard.invoke()
                    if (title == "Danh sách sinh viên"){
                        viewModel.onItemStudentSelect(action.selectedItem)
                    }else{
                        viewModel.onItemTeacherSelect(action.selectedItem)
                    }
                }
            }

            is UserUIAction.OnAddressChange -> {
                scope.launch {
                    if (title == "Danh sách sinh viên"){
                        viewModel.getChangePredictionsStudent(action.value)
                    }else{
                        viewModel.getChangePredictionsTeacher(action.value)
                    }

                }
            }

            is UserUIAction.OnAddressAutoCompleteDone -> {

                hideKeyboard.invoke()

            }

            is UserUIAction.OnAddressAutoCompleteClear -> {

                if (title == "Danh sách sinh viên"){
                    viewModel.onAutoCompleteClearStudent(action.userSelect)
                }else{
                    viewModel.onAutoCompleteClearTeacher(action.userSelect)
                }

            }
        }

    }
}


@Composable
fun RowAutoCompleteTextView(
    title: String,
    selected: MutableState<String>,
    predictionsUser: SnapshotStateList<String>,
    listUser: SnapshotStateList<String>,
    addressUIAction: (UserUIAction) -> Unit

) {

    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = selected.value,
        queryLabel = title,
        onQueryChanged = { updatedAddress ->
            selected.value = updatedAddress
            //Todo: call the view model method to update addressPlaceItemPredictions
            addressUIAction(UserUIAction.OnAddressChange(updatedAddress))
        },
        predictions = predictionsUser,
        onClearClick = {
            predictionsUser.addAll(listUser)
            addressUIAction(UserUIAction.OnAddressAutoCompleteClear(selected))

            //Todo: call the view model method to clear the predictions

        },
        onDoneActionClick = {
            addressUIAction(UserUIAction.OnAddressAutoCompleteDone)
        },
        onItemClick = { placeItem ->
            addressUIAction(
                UserUIAction.OnAddressSelected(
                    placeItem
                )
            )

            //Todo: call the view model method to update the UI with the selection

        },
        list = listUser
    ) {

        //Define how the items need to be displayed
        Text(it, fontSize = 14.sp)

    }
}

@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: SnapshotStateList<T>,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    list: SnapshotStateList<T>,
    itemContent: @Composable (T) -> Unit = {}
) {

    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = modifier.heightIn(max = TextFieldDefaults.MinHeight * 6),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            QuerySearch(
                query = query,
                label = queryLabel,
                onQueryChanged = onQueryChanged,
                onDoneActionClick = {
                    view.clearFocus()
                    onDoneActionClick()
                },
                onClearClick = {
                    onClearClick()
                },
                predictions = predictions,
                list = list
            )
        }

        items(predictions)
        { prediction ->
            Row(
                Modifier
                    .padding(top = 8.dp, start = 63.dp)
                    .fillMaxWidth()
                    .clickable {
                        view.clearFocus()
                        onItemClick(prediction)
                    },
            ) {
                itemContent(prediction)
            }
        }

    }


}
