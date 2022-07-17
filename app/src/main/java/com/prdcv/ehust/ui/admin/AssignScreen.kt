package com.prdcv.ehust.ui.admin

import androidx.compose.foundation.layout.*
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
fun AssignScreen(viewModel: AssignViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllProjectCurrentSemester()
        viewModel.getInformationDashBoard()
    }

    val uiState = viewModel.uiState

    DefaultTheme {
        Scaffold(scaffoldState = rememberScaffoldState(snackbarHostState = viewModel.snackbarHostState)) {
            ToolBarAssign(title = "Trang chủ")
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpinnerProject(
                    label = "Danh sách project",
                    options = uiState.subjects,
                    selectedOption = uiState.selectedSubject,
                    onItemClick = viewModel::onProjectSelected
                )
                Spacer(modifier = Modifier.height(16.dp))

                SpinnerStudent(
                    label = "Danh sách Sinh vien",
                    options = uiState.students,
                    selectedOption = uiState.selectedStudent,
                    onItemClick = viewModel::onStudentSelected
                )
                Spacer(modifier = Modifier.height(16.dp))

                SpinnerTeacher(
                    label = "Danh sách giang vien",
                    options = uiState.teachers,
                    selectedOption = uiState.selectedTeacher,
                    onItemClick = viewModel::onTeacherSelected
                )
                Spacer(modifier = Modifier.height(45.dp))

                Button(onClick = viewModel::onSubmit, enabled = uiState.submitButtonEnabled) {
                    Text(
                        text = "Submit",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(15.dp, 6.dp)
                    )
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
                .weight(4f)
        )
        Text(
            text = "Log out",
            modifier = Modifier.weight(1f),
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