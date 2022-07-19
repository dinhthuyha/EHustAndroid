package com.prdcv.ehust.ui.admin

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.prdcv.ehust.R
import com.prdcv.ehust.model.PairingStudentWithTeacher
import com.prdcv.ehust.model.TaskData
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.projects.SpinnerSemester
import com.prdcv.ehust.ui.task.CircularProgressWithPercent
import com.prdcv.ehust.viewmodel.AssignViewModel
import com.prdcv.ehust.viewmodel.TaskStatus
import java.time.LocalDate
import java.time.Period
import java.util.*

@Preview(showBackground = true)
@Composable
fun ManagementScreen(
    viewModel: AssignViewModel = hiltViewModel(),
    navController: NavController? = null,
    hideKeyboard: () -> Unit = {}
) {
    val uiState = viewModel.uiState
    uiState.semesterStatus.value = uiState.informationDashBoard.value.semester ?: 0
    val textStateFilterUser = remember { mutableStateOf("") }
    val textStateFilterProject = remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchDataManagementScreen()
    }
    DefaultTheme() {
        Scaffold(
            scaffoldState = rememberScaffoldState(snackbarHostState = viewModel.snackbarHostState),
            topBar = {
                TopAppBar(backgroundColor = colorResource(id = R.color.text_color)) {
                    Text(
                        text = "Quản lý",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }) {
            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp, start = 12.dp)
                        .placeholder(
                            visible = uiState.refreshState.isRefreshing,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                ) {
                    Text(
                        text = "Học kỳ: ",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                    )
                    SpinnerSemester(
                        options = uiState.listSemester,
                        selectedOption = uiState.semesterStatus,
                        onItemClick = viewModel::onSemesterSelected,
                        isLoading = uiState.refreshState.isRefreshing
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Column(
                    horizontalAlignment = Alignment.End, modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Lọc sinh viên/ giảng viên theo tên:",
                        modifier = Modifier.placeholder(
                            visible = uiState.refreshState.isRefreshing,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.weight(0.45f)
                        ) {
                            if (textStateFilterUser.value != "")
                                Button(
                                    onClick = {
                                        viewModel.getProfileByUser(
                                            textStateFilterUser.value,
                                            navController
                                        )


                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.LightGray
                                    ),
                                    content = {
                                        Text(
                                            text = "Thông tin chi tiết",
                                            fontSize = 11.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.W300
                                        )
                                    },
                                    modifier = Modifier
                                        .height(32.dp)
                                        .widthIn(50.dp, 150.dp)
                                        .placeholder(
                                            visible = uiState.refreshState.isRefreshing,
                                            highlight = PlaceholderHighlight.shimmer()
                                        )
                                )
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5f), horizontalAlignment = Alignment.End
                        ) {
                            RowFilter(
                                isLoading = uiState.refreshState.isRefreshing,
                                viewModel = viewModel,
                                textState = textStateFilterUser,
                                data = viewModel.uiState.listFullNameUser,
                                hideKeyboard = hideKeyboard
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(12.dp))


                    Text(
                        text = "Lọc danh sách đồ án:",
                        modifier = Modifier.placeholder(
                            visible = uiState.refreshState.isRefreshing,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    RowFilter(
                        isLoading = uiState.refreshState.isRefreshing,
                        viewModel = viewModel,
                        textState = textStateFilterProject,
                        data = viewModel.uiState.listProject,
                        hideKeyboard = hideKeyboard
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                }
                if (uiState.refreshState.isRefreshing) {
                    (1..8).forEach {
                        TableRowReplace(isLoading = true)
                    }

                } else
                    TableScreen(
                        viewModel = viewModel,
                    )
            }
        }
    }
}

@Composable
fun RowFilter(
    isLoading: Boolean = false,
    viewModel: AssignViewModel,
    textState: MutableState<String>,
    hideKeyboard: () -> Unit,
    data: SnapshotStateList<String>
) {

    val visibleSuggest: MutableState<Boolean> = remember {
        mutableStateOf(true)
    }
    SearchView(textState, isLoading = isLoading, visibleSuggest) {
        viewModel.fetchDataManagementScreen()
    }
    Log.d("TAG", "RowFilter: ${textState.value}, ${visibleSuggest.value}")
    if (textState.value != "")
        if (visibleSuggest.value)
            FullNameList(
                state = textState,
                viewModel = viewModel,
                visible = visibleSuggest,
                data = data,
                hideKeyboard = hideKeyboard
            )
}

@Composable
fun FullNameList(
    state: MutableState<String>,
    viewModel: AssignViewModel,
    visible: MutableState<Boolean>,
    data: SnapshotStateList<String>,
    hideKeyboard: () -> Unit
) {
    var filteredFullName: SnapshotStateList<String> = mutableStateListOf()
    LazyColumn(modifier = Modifier.widthIn(min = 70.dp, max = 180.dp)) {
        val searchedText = state.value
        filteredFullName = if (searchedText.isEmpty()) {
            data
        } else {
            val resultList: SnapshotStateList<String> = mutableStateListOf()
            for (country in data) {
                if (country.lowercase(Locale.getDefault())
                        .contains(searchedText.lowercase(Locale.getDefault()))
                ) {
                    resultList.add(country)
                }
            }
            resultList
        }
        items(filteredFullName) { filteredList ->
            UserListItem(
                countryText = filteredList,
                onItemClick = { selectedName ->

                    visible.value = false
                    Log.d("TAG", "click: ${visible.value}")
                    state.value = selectedName
                    filteredFullName.clear()
                    viewModel.filterItem(selectedName)
                    hideKeyboard.invoke()

//                    navController.navigate("details/$selectedCountry") {
//                        // Pop up to the start destination of the graph to
//                        // avoid building up a large stack of destinations
//                        // on the back stack as users select items
//                        popUpTo("main") {
//                            saveState = true
//                        }
//                        // Avoid multiple copies of the same destination when
//                        // reselecting the same item
//                        launchSingleTop = true
//                        // Restore state when reselecting a previously selected item
//                        restoreState = true
//                    }
                }
            )
        }
    }
}


@Composable
fun UserListItem(countryText: String, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = { onItemClick(countryText) })
            .background(color = Color.LightGray)
            .height(57.dp)
            .fillMaxWidth()
            .padding(PaddingValues(8.dp, 16.dp))
    ) {
        Text(text = countryText, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
fun SearchView(
    state: MutableState<String>,
    isLoading: Boolean = false,
    visible: MutableState<Boolean>,
    showAllData: () -> Unit
) {
    Row(
        modifier = Modifier
            .border(
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(12),
            )
            .placeholder(
                visible = isLoading,
                highlight = PlaceholderHighlight.shimmer()
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
            },
            modifier = Modifier
                .widthIn(min = 70.dp, max = 180.dp)
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
                .padding(5.dp),
            singleLine = true
        )
        if (state.value != "") {
            if (!visible.value)

                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        state.value = ""
                        showAllData.invoke()
                    }
                )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun TableRowReplace(
    data: TaskData = TaskData(),
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    fun showTimeRemain(): String {
        if (data.status == TaskStatus.IN_PROGRESS) {
            val today = LocalDate.now()
            val dueDate = data.dueDate
            val dateRemain = Period.between(today, dueDate).days
            return "(in $dateRemain days)"
        }

        return ""
    }
    Card(
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CircularProgressWithPercent(progress = data.progress, isLoading = isLoading)
            Text(
                text = "${data.dueDate} ${showTimeRemain()}",
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                modifier = Modifier.placeholder(
                    visible = isLoading,
                    highlight = PlaceholderHighlight.shimmer()
                )
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "${data.dueDate} ${showTimeRemain()}",
                fontWeight = FontWeight.Light,
                fontSize = 13.sp,
                modifier = Modifier.placeholder(
                    visible = isLoading,
                    highlight = PlaceholderHighlight.shimmer()
                )
            )
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(10.dp)
            ) {
                Text(
                    text = data.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(bottom = 20.dp, top = 2.dp)
                        .placeholder(
                            visible = isLoading,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )

            }

        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    val end = Border(0.5.dp, Color.Black)
    val borders = Borders(bottom = end, top = end, end = end)
    Row(
        modifier = Modifier
            .weight(weight)
            .padding(start = 8.dp)
            .border(end = borders.end),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            Modifier
                .weight(weight)
        )
    }

}

@Composable
fun RowScope.RowCheckBox(
    weight: Float,
    onItemChecked: (Boolean) -> Unit = {},
    checked: Boolean = false
) {
    val end = Border(0.5.dp, Color.Black)
    val borders = Borders(bottom = end, top = end, end = end)
    val checkedState = remember { mutableStateOf(checked) }
    Row(
        modifier = Modifier
            .weight(weight)
            .border(end = borders.end)
    ) {
        Checkbox(
            // below line we are setting
            // the state of checkbox.
            checked = checkedState.value,
            // below line is use to add padding
            // to our checkbox.


            // below line is use to add on check
            // change to our checkbox.
            onCheckedChange = {
                checkedState.value = it
                onItemChecked(it)
            },
        )
    }


}


@Composable
fun TableScreen(
    viewModel: AssignViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val tableData = viewModel.uiState.tableData
    val totalPage = if (tableData.size % 10 == 0) tableData.size / 10 else (tableData.size / 10) + 1
    var currentPage by mutableStateOf(1)

    // Each cell of a column must have the same weight.
    val column1Weight = .1f // 30%
    val column2Weight = .25f // 70%
    val column3Weight = .4f // 70%

    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(Modifier.fillMaxSize()) {
        // Here is the header
        item {
            Row(
                Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .height(30.dp)

            ) {
                TableCell(text = "", weight = column1Weight)
                TableCell(text = "Sinh viên", weight = column2Weight)
                TableCell(text = "Giảng viên", weight = column2Weight)
                TableCell(text = "Đồ án", weight = column3Weight)

            }
        }

        // Here are all the lines of your table.
        val temps = mutableListOf<PairingStudentWithTeacher>()
        val count: Int = if (currentPage == totalPage) tableData.size - 8 * (currentPage - 1) else 8
        if (tableData.isNotEmpty()) {
            val items = if (count == 8 && tableData.size != 0) {
                tableData.slice(8 * (currentPage - 1) until 8 * (currentPage - 1) + count)
            } else {
                tableData.slice(8 * (currentPage - 1) until 8 * (currentPage - 1) + count)
            }
            temps.addAll(items)

        }
        val listFilter: SnapshotStateList<PairingStudentWithTeacher> = mutableStateListOf()
        listFilter.addAll(temps)
        items(listFilter) { t ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(0.5.dp, Color.Black)
                    .heightIn(min = 40.dp, max = 100.dp)

            ) {
                RowCheckBox(
                    weight = column1Weight,
                    onItemChecked = {
                        if (it) viewModel.onItemChecked(t) else viewModel.onItemUnChecked(t)
                    })
                TableCell(text = t.nameStudent, weight = column2Weight)
                TableCell(text = t.nameTeacher, weight = column2Weight)
                TableCell(text = t.nameProject, weight = column3Weight)
            }
        }
        item {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp, top = 8.dp)

            ) {
                Button(
                    onClick = {
                        Log.d("TAG", "click button xoa: ${viewModel.uiState.listItemChecked.size}")
                        viewModel.deleteItemChecked()
                    },
                    content = {
                        Text(
                            text = "Xoá",
                            style = MaterialTheme.typography.button,
                            color = Color.White,

                            )
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = com.prdcv.ehust.ui.compose.Button
                    )
                )
            }

        }
        item {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp, top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        listFilter.clear()
                        if (currentPage > 1) currentPage--
                    }
                )
                Text(
                    text = "Trang ${currentPage}/${totalPage}",
                    modifier = Modifier.padding(end = 10.dp, start = 10.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        if (currentPage < totalPage) {
                            listFilter.clear()
                            currentPage++
                        }
                    }
                )
            }
        }
    }
}

