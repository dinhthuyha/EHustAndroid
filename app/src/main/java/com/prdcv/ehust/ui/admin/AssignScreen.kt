package com.prdcv.ehust.ui.admin

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
fun AssignScreen(viewModel: AssignViewModel, context: Context) {
    DefaultTheme {

        Scaffold() {
            var selectedOptionProjects = remember { mutableStateOf("") }
            var selectedNewOProjects = remember { mutableStateOf(false) }
            var selectedOptionStudents = remember { mutableStateOf("") }
            var selectedOptionTeachers = remember { mutableStateOf("") }
            var projects = remember { mutableStateOf(mutableListOf<String>()) }
            var fullNameTeachers = remember { mutableStateOf(mutableListOf<String>()) }
            var fullNameStudents = remember { mutableStateOf(mutableListOf<String>()) }
            var teachers = remember { mutableStateOf(mutableListOf<User>()) }
            var students = remember { mutableStateOf(mutableListOf<User>()) }

            ToolBarAssign(title = "Trang chủ ")
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (val pro = viewModel.projectsState.value) {
                    is State.Success -> {
                        projects.value.addAll(pro.data.map { it.name })
                        SpinnerProject(
                            label = "Danh sách project",
                            options = projects.value,
                            selectedOptionText = selectedOptionProjects,
                            viewModel = viewModel
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        SpinnerStudent(
                            label = "Danh sách Sinh vien",
                            callback = {
                                when (val tea = viewModel.studentsState.value) {
                                    is State.Success -> {
                                        students.value.clear()
                                        students.value.addAll(tea.data)
                                        fullNameStudents.value.addAll(tea.data.map { it.fullName!! })
                                    }
                                }
                            },
                            options = fullNameStudents.value,
                            selectedOptionText = selectedOptionStudents
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SpinnerTeacher(
                            label = "Danh sách giang vien",
                            callback = {
                                when (val tea = viewModel.teachersState.value) {
                                    is State.Success -> {
                                        teachers.value.clear()
                                        teachers.value.addAll(tea.data)
                                        fullNameTeachers.value.addAll(tea.data.map { it.fullName!! })
                                    }
                                }


                            },
                            options = fullNameTeachers.value,
                            selectedOptionText = selectedOptionTeachers
                        )
                        Spacer(modifier = Modifier.height(45.dp))
                        Button(
                            onClick = {
                            },

                            ) {
                            Text(
                                text = "Submit",
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(15.dp, 6.dp)
                                    .clickable {
                                        if (!selectedOptionProjects.value.equals("")
                                            && !selectedOptionStudents.value.equals("")
                                            && !selectedOptionTeachers.value.equals("")
                                        ) {

                                            val idStudent = students.value.firstOrNull { it.fullName == selectedOptionStudents.value }!!.id
                                            val idTeacher = teachers.value.firstOrNull { it.fullName == selectedOptionTeachers.value }!!.id
                                            viewModel.assign(idStudent, idTeacher, selectedOptionStudents.value)
                                            Log.d("TAG", "AssignScreen: ${fullNameTeachers.value.size}")
                                        }
                                    }
                            )
                        }
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
                .weight(4f)
        )
        Text(
            text = " Log out",
            modifier = Modifier.weight(1f),
            color = White
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SpinnerProject(
    label: String,
    options: List<String>,
    selectedOptionText: MutableState<String>,
    viewModel: AssignViewModel
) {
    var expanded = remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {

        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText.value,
            onValueChange = {},
            label = { Text("${label}") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple500,
                unfocusedBorderColor = LightGray
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )
            },


            )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.getAllUserInClass(selectionOption, Role.ROLE_TEACHER)
                        viewModel.getAllUserInClass(selectionOption, Role.ROLE_STUDENT)
                        selectedOptionText.value = selectionOption
                        expanded.value = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SpinnerTeacher(
    label: String,
    callback: () -> Unit,
    options: List<String>,
    selectedOptionText: MutableState<String>
) {
    val focusManager = LocalSoftwareKeyboardController.current
    var expanded = remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
            focusManager?.hide()
        }
    ) {
        callback.invoke()
        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText.value,
            onValueChange = {},
            label = { Text("${label}") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple500,
                unfocusedBorderColor = LightGray
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )
            },
            keyboardActions = KeyboardActions(onDone = { focusManager?.hide() }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.None
            ),

            )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText.value = selectionOption
                        expanded.value = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SpinnerStudent(
    label: String,
    callback: () -> Unit,
    options: List<String>,
    selectedOptionText: MutableState<String>
) {
    val focusManager = LocalSoftwareKeyboardController.current
    var expanded = remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
            focusManager?.hide()
        }
    ) {
        callback.invoke()
        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText.value,
            onValueChange = {},
            label = { Text("${label}") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple500,
                unfocusedBorderColor = LightGray
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded.value
                )
            },
            keyboardActions = KeyboardActions(onDone = { focusManager?.hide() }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.None
            ),

            )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText.value = selectionOption
                        expanded.value = false
                    }
                ) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}
