package com.prdcv.ehust.ui.admin

import android.util.Log
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prdcv.ehust.R
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.viewmodel.AssignViewModel

@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit
) {


    var showClearButton by remember { mutableStateOf(false) }


    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                showClearButton = (focusState.isFocused)
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
fun a(viewModel: AssignViewModel = androidx.lifecycle.viewmodel.compose.viewModel()){
    val uiState = viewModel.uiState
    val addressPlaceItemPredictions =
        listOf<String>("hà nội", "thành phố hồ chí minh", "nha trang"," vũng tàu", "thanh ho")

    DefaultTheme() {
        Scaffold() {
            AutoCompleteTextView(
                modifier = Modifier.fillMaxWidth(),
                query = uiState.streetAddress.value,
                queryLabel = stringResource(id = R.string.location_section_address),
                onQueryChanged = { updatedAddress ->
                    Log.d("TAG", "AssignScreen: ${updatedAddress}")
                    uiState.streetAddress.value = updatedAddress
                    //Todo: call the view model method to update addressPlaceItemPredictions
                },
                predictions = addressPlaceItemPredictions,
                onClearClick = {

                    uiState.streetAddress.value  = ""

                    //Todo: call the view model method to clear the predictions

                },
                onDoneActionClick = {  },
                onItemClick = { placeItem ->

                    //Todo: call the view model method to update the UI with the selection

                }
            ) {

                //Define how the items need to be displayed
                Text(it, fontSize = 14.sp)

            }
        }
    }
}
@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {}
) {

    val view = LocalView.current
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = modifier.heightIn(max = TextFieldDefaults.MinHeight * 6)
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
                }
            )
        }

        items(predictions)
        { prediction ->
            Row(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        view.clearFocus()
                        onItemClick(prediction)
                    }
            ) {
                itemContent(prediction)
            }
        }

    }


}
