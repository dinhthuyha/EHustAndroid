package com.prdcv.ehust.ui.admin

import android.util.Log
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
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Purple500
import com.prdcv.ehust.viewmodel.AssignViewModel

@Composable
fun AssignScreen(viewModel: AssignViewModel) {

    LaunchedEffect(key1 = Unit) {
        viewModel.getAllProjectCurrentSemester()
    }

    val subjectList by viewModel.projectsState.collectAsState()
    val studentList by viewModel.studentsState.collectAsState()
    val teacherList by viewModel.teachersState.collectAsState()

    val selectedOptionProjects = remember { mutableStateOf("") }
    var selectedNewOProjects = remember { mutableStateOf(false) }
    val selectedOptionStudents = remember { mutableStateOf("") }
    val selectedOptionTeachers = remember { mutableStateOf("") }
    val projects by remember { mutableStateOf(mutableListOf<String>()) }
    val fullNameTeachers by remember { mutableStateOf(mutableListOf<String>()) }
    val fullNameStudents by remember { mutableStateOf(mutableListOf<String>()) }
    val teachers = remember { mutableStateOf(mutableListOf<User>()) }
    val students = remember { mutableStateOf(mutableListOf<User>()) }

    DefaultTheme {
        Scaffold {
            ToolBarAssign(title = "Trang chủ")
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val state = subjectList) {
                    is State.Success -> {
                        projects.clear()
                        projects.addAll(state.data.map { it.name })
                        SpinnerProject(
                            label = "Danh sách project",
                            options = projects,
                            selectedOptionText = selectedOptionProjects,
                            onItemClick = { projectName ->
                                viewModel.getAllUserInClass(projectName, Role.ROLE_TEACHER)
                                viewModel.getAllUserInClass(projectName, Role.ROLE_STUDENT)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        when (val std = studentList) {
                            is State.Success -> {
                                students.value.clear()
                                students.value.addAll(std.data)
                                selectedOptionStudents.value = ""
                                fullNameStudents.clear()
                                fullNameStudents.addAll(std.data.map { it.fullName!! })

                                SpinnerStudent(
                                    label = "Danh sách Sinh vien",
                                    options = fullNameStudents,
                                    selectedOptionText = selectedOptionStudents
                                )
                            }
                            else -> {}
                        }
                        Spacer(modifier = Modifier.height(16.dp))


                        when (val tea = teacherList) {
                            is State.Success -> {
                                teachers.value.clear()
                                teachers.value.addAll(tea.data)
                                selectedOptionTeachers.value = ""
                                fullNameTeachers.clear()
                                fullNameTeachers.addAll(tea.data.map { it.fullName!! })

                                SpinnerTeacher(
                                    label = "Danh sách giang vien",
                                    options = fullNameTeachers,
                                    selectedOptionText = selectedOptionTeachers
                                )
                            }
                            else -> {}
                        }
                        Spacer(modifier = Modifier.height(45.dp))

                        Button(onClick = {
                            if (selectedOptionProjects.value != ""
                                && selectedOptionStudents.value != ""
                                && selectedOptionTeachers.value != ""
                            ) {

                                val idStudent =
                                    students.value.firstOrNull { it.fullName == selectedOptionStudents.value }!!.id
                                val idTeacher =
                                    teachers.value.firstOrNull { it.fullName == selectedOptionTeachers.value }!!.id
                                viewModel.assign(
                                    idStudent,
                                    idTeacher,
                                    selectedOptionStudents.value
                                )
                                Log.d(
                                    "TAG",
                                    "AssignScreen: ${fullNameTeachers.size}"
                                )
                            }
                        }) {
                            Text(
                                text = "Submit",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(15.dp, 6.dp)
                            )
                        }
                    }
                    is State.Error -> Text(text = "Error: ${state.exception}")
                    State.Loading -> Text(text = "Loading...")
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
    options: List<String>,
    selectedOptionText: MutableState<String>,
    onItemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {

        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText.value,
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
                        selectedOptionText.value = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpinnerTeacher(
    label: String,
    options: List<String>,
    selectedOptionText: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText.value,
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
                        selectedOptionText.value = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpinnerStudent(
    label: String,
    options: List<String>,
    selectedOptionText: MutableState<String>
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText.value,
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
                        selectedOptionText.value = selectionOption
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}
