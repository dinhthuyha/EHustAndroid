package com.prdcv.ehust.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.hadt.ehust.model.TypeSubject
import com.prdcv.ehust.R
import com.prdcv.ehust.data.model.ProgressStatus
import com.prdcv.ehust.data.model.Role
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Purple500
import com.prdcv.ehust.ui.compose.Shapes


@Composable
fun InformationTopic(idTopic: Int = 0, viewModel: TopicsViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.moreInformationTopic
    val state = mutableStateOf("")
    val loading = viewModel.uiState.refreshState
    val readOnly = viewModel.uiState.readOnly
    val isEditing= viewModel.uiState.isEditing


    LaunchedEffect(key1 = Unit) {
        viewModel.findDetailInformationTopic(idTopic)
    }
    when (viewModel.mRole) {
        Role.ROLE_STUDENT -> {
            viewModel.uiState.readOnly.value = true
        }
        Role.ROLE_TEACHER -> {
            viewModel.uiState.readOnly.value = false
        }
        else -> {}
    }
    DefaultTheme() {
        Scaffold(topBar = {
            ToolBar(
                title = "Thông tin chi tiết đề tài",
                isEditing.value,
                onSaveTask = viewModel::saveInformationTopic,
                onEditTask = {isEditing.value = false},
                readOnly
            )
        }) {

            Log.d("TAG", "read Only:${readOnly.value} ")
            LazyColumn(modifier = Modifier.padding(start = 10.dp, top = 10.dp)) {
                if (loading.isRefreshing) {
                    items(count = 7) {
                        RowHOlder()
                    }
                } else {
                    item {
                        Column {
                            Row(modifier = Modifier.padding(bottom = 10.dp)) {
                                Text(
                                    text = "Tên đề tài:",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = uiState.value.title ?: "",
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            Text(
                                text = "Mô tả đề tài: ",
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 10.dp)

                            )
                            Text(
                                text = uiState.value.description ?: "",
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 10.dp)

                            )

                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Thời gian thực hiện: ",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Text(
                                    text = "${uiState.value.startTimeProgress.toString()} - ${uiState.value.dueTime.toString()}",
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                            }

                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Giảng viên hướng dẫn: ",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Text(
                                    text = uiState.value.nameTeacher ?: "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                            }
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Email: ", style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Text(
                                    text = uiState.value.emailTeacher ?: "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                            }

                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Sinh viên thực hiện: ",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Text(
                                    text = uiState.value.nameStudent ?: "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                            }
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Email: ",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                                Text(
                                    text = uiState.value.emailStudent ?: "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                )
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 8.dp, start = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                when (uiState.value.type) {
                                    TypeSubject.PROJECT -> {
                                        Column {
                                            Row() {
                                                Text(text = "Điểm quá trình:")
                                                Spacer(modifier = Modifier.width(10.dp))
                                                BasicTextField(
                                                    value = "10",
                                                    onValueChange = { value ->
                                                        state.value = value
                                                    },

                                                    readOnly = readOnly.value,
                                                    modifier = Modifier
                                                        .widthIn(min = 40.dp, max = 50.dp)
                                                        .heightIn(min = 40.dp, max = 130.dp)
                                                        .border(
                                                            1.dp,
                                                            Color.LightGray,
                                                            Shapes.medium
                                                        )
                                                        .padding(top = 10.dp, start = 10.dp),
                                                    singleLine = true
                                                )
                                            }
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(text = "Điểm cuối kì:")
                                                Spacer(modifier = Modifier.width(10.dp))
                                                BasicTextField(
                                                    value = "10",
                                                    onValueChange = { value ->
                                                        state.value = value
                                                    },

                                                    readOnly = readOnly.value,
                                                    modifier = Modifier
                                                        .widthIn(min = 40.dp, max = 50.dp)
                                                        .heightIn(min = 40.dp, max = 130.dp)
                                                        .border(
                                                            1.dp,
                                                            Color.LightGray,
                                                            Shapes.medium
                                                        )
                                                        .padding(top = 10.dp, start = 10.dp),
                                                    singleLine = true
                                                )
                                            }
                                        }
                                    }
                                    TypeSubject.THESIS -> {
                                        Text(
                                            text = "Trạng thái: ",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        SpinnerStatusProcess(
                                            options = viewModel.uiState.listStatusProcess,
                                            selectedOption = viewModel.uiState.statusProcess,
                                            onItemClick = viewModel::onStatusProcessSelected,
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    else -> {}
                                }

                            }


                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ToolBar(
    title: String?,
    isEditing: Boolean,
    onSaveTask: () -> Unit,
    onEditTask: () -> Unit,
    readOnly: MutableState<Boolean>

) {
    TopAppBar(
        title = {
            Text(text = title ?: "Detail task")
        },
        backgroundColor = colorResource(id = R.color.text_color),
        contentColor = Color.White,
        elevation = 2.dp,
        actions = {

            if (!readOnly.value) {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(if (!isEditing) Icons.Filled.Check else Icons.Filled.Edit,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            when (isEditing) {
                                true -> onEditTask()
                                false -> onSaveTask()
                            }
                        }
                    )
                }
            }

        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpinnerStatusProcess(
    options: List<ProgressStatus>,
    selectedOption: MutableState<ProgressStatus>,
    onItemClick: (ProgressStatus) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .width(165.dp)
            .height(60.dp)
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption.value?.toString() ?: "",
            textStyle = MaterialTheme.typography.caption,
            onValueChange = {},
            label = { },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.LightGray
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },

            )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onItemClick(selectionOption)
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption.toString(), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun RowHOlder() {
    Text(
        text = "Xây dựng ứng dụng quản lí đồ án trên nền tảng Android.\n" +
                "Hệ thống bao gồm:\n",
        modifier = Modifier
            .padding(16.dp)
            .placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer()
            )

    )
}
