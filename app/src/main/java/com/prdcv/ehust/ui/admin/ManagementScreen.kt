package com.prdcv.ehust.ui.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.prdcv.ehust.R
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.projects.SpinnerSemester
import com.prdcv.ehust.viewmodel.AssignViewModel
import kotlinx.coroutines.delay

@Composable
fun ManagementScreen(viewModel: AssignViewModel) {
    val uiState = viewModel.uiState
    val dashboardInfo by uiState.informationDashBoard
    uiState.semesterStatus.value = dashboardInfo.semester

    DefaultTheme() {
        Scaffold(topBar = {
            TopAppBar(backgroundColor = colorResource(id = R.color.text_color)) {
                Text(
                    text = "Quản lí",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }) {
            Column() {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 12.dp, bottom = 12.dp, start = 12.dp)
                ) {
                    Text(
                        text = "Học kì: ",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    SpinnerSemester(
                        options = uiState.listSemester,
                        selectedOption = uiState.semesterStatus,
                        onItemClick = viewModel::onSemesterSelected
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
//                Column(horizontalAlignment = Alignment.End) {
//                    Text(text = "Lọc sinh viên/ giảng viên theo tên:")
//                    RowComplete(
//                        viewModel = viewModel,
//                        title = "",
//                        selected = viewModel.uiState.userSelect,
//                        predictionsUser = viewModel.uiState.predictionsUser,
//                        listUser = viewModel.uiState.listFullNameUser,
//                        hideKeyboard = {}
//                    )
//                }


                TableScreen(viewModel = viewModel)
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
            .height(30.dp)
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
fun RowScope.RowCheckBox(weight: Float, onItemChecked: () -> Unit, checked: Boolean =false) {
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
                if (it) {
                    onItemChecked.invoke()
                }
            },
        )
    }


}

@Preview(showBackground = true)
@Composable
fun TableScreen(viewModel: AssignViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val tableData = viewModel.uiState.tableData
    val numberPager =
        if (tableData.size % 10 == 0) tableData.size / 10 else (tableData.size / 10) + 1
    var number = mutableStateOf(1)
    // Each cell of a column must have the same weight.
    val column1Weight = .1f // 30%
    val column2Weight = .3f // 70%
    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        Modifier
            .fillMaxSize()

    ) {
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
                TableCell(text = "Đồ án", weight = column2Weight)

            }
        }
        // Here are all the lines of your table.
        val count: MutableState<Int> = mutableStateOf(0)
        count.value =
            if (number.value == numberPager) tableData.size - 10 * (number.value - 1) else 10
        val temps = if (count.value == 10) {
            tableData.slice(10 * (number.value - 1)..(10 * (number.value - 1) + count.value - 1))
        } else {
            tableData.slice(10 * (number.value - 1)..(10 * (number.value - 1) + count.value - 1))
        }
        val listFilter: SnapshotStateList<String> = mutableStateListOf()
        listFilter.addAll(temps)
        items(listFilter) { t ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(0.5.dp, Color.Black)
                    .height(40.dp)
            ) {
                RowCheckBox(weight = column1Weight, onItemChecked = { viewModel.onItemChecked(t) },)
                TableCell(text = t, weight = column2Weight)
                TableCell(text = t, weight = column2Weight)
                TableCell(text = t, weight = column2Weight)
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
                        viewModel.deleteItemChecked(listFilter)
                        viewModel.uiState.listItemChecked.clear()
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
        item() {
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
                        if (number.value > 1) number.value--
                    }
                )
                Text(
                    text = "Số page ${number.value}/${numberPager}",
                    modifier = Modifier.padding(end = 10.dp, start = 10.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "",
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        listFilter.clear()
                        if (number.value < tableData.size) number.value++
                    }
                )
            }
        }
    }
}

