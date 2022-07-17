package com.prdcv.ehust.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prdcv.ehust.R
import com.prdcv.ehust.model.Subject
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Purple500
import com.prdcv.ehust.viewmodel.AssignViewModel

@Composable
fun AssignScreen(viewModel: AssignViewModel, hideKeyboard: () -> Unit) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllProjectCurrentSemester()
        viewModel.getInformationDashBoard()
    }

    val uiState = viewModel.uiState

    DefaultTheme {
        Scaffold(scaffoldState = rememberScaffoldState(snackbarHostState = viewModel.snackbarHostState)) {
            ToolBarAssign(title = "Trang chủ")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    SpinnerProject(
                        label = "Danh sách dự án",
                        options = uiState.subjects,
                        selectedOption = uiState.selectedSubject,
                        onItemClick = viewModel::onProjectSelected
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        RowComplete(viewModel, title = "Danh sách sinh viên",selected = uiState.studentSelect, predictionsUser = uiState.predictionsStudent, listUser = uiState.listFullNameStudent, hideKeyboard = hideKeyboard)
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        RowComplete(viewModel, title = "Danh sách giảng viên",selected = uiState.teacherSelect, predictionsUser = uiState.predictionsTeacher, listUser = uiState.listFullNameTeacher, hideKeyboard = hideKeyboard)
                    }
                }

                item { Spacer(modifier = Modifier.height(45.dp)) }

                item {
                    Button(onClick = viewModel::onSubmit, enabled = uiState.submitButtonEnabled) {
                        Text(
                            text = "Gán dự án",
                            fontSize = 16.sp,
                            color = White,
                            modifier = Modifier.padding(15.dp, 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToolBarAssign(title: String) {
    TopAppBar(backgroundColor = colorResource(id = R.color.text_color)) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Đăng xuất",
            color = White
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpinnerProject(
    label: String,
    options: List<Subject>,
    selectedOption: Subject?,
    onItemClick: (Subject) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {

        OutlinedTextField(
            readOnly = true,
            value = selectedOption?.name ?: "",
            onValueChange = {},
            label = { Text(label) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple500,
                unfocusedBorderColor = LightGray
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
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
                    Text(text = selectionOption.name)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpinnerStudent(
    label: String,
    options: List<User>,
    selectedOption: User?,
    onItemClick: (User) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption?.fullName ?: "",
            onValueChange = {},
            label = { Text(label) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple500,
                unfocusedBorderColor = LightGray
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
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
                    Text(text = selectionOption.fullName ?: "")
                }
            }
        }
    }
}

@Composable
fun SpinnerTeacher(
    label: String,
    options: List<User>,
    selectedOption: User?,
    onItemClick: (User) -> Unit
) = SpinnerStudent(
    label = label,
    options = options,
    selectedOption = selectedOption,
    onItemClick = onItemClick,
)