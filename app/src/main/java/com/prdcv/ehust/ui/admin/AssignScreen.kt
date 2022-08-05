package com.prdcv.ehust.ui.admin

import android.content.SharedPreferences
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import com.prdcv.ehust.R
import com.prdcv.ehust.data.model.Subject
import com.prdcv.ehust.data.model.User
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Purple500
import com.prdcv.ehust.utils.SharedPreferencesKey
import com.prdcv.ehust.viewmodel.HomeAdminViewModel
import org.bouncycastle.asn1.x500.style.RFC4519Style.title

@Composable
fun AssignScreen(viewModel: HomeAdminViewModel, navController: NavController, sharedPreferences: SharedPreferences, hideKeyboard: () -> Unit) {


    val uiState = viewModel.uiState

    DefaultTheme {
        Scaffold(
            scaffoldState = rememberScaffoldState(snackbarHostState = viewModel.snackbarHostState),
            topBar = { ToolBarAssign(title = "Trang chủ") {
                navController.popBackStack()
                sharedPreferences.edit().remove(SharedPreferencesKey.TOKEN).commit()
                uiState.resetSelectionState()
            } })
        {

            LaunchedEffect(Unit) {
                uiState.resetSelectionState()
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    SpinnerProject(
                        label = "Danh sách đồ án",
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
                            text = "Gán đồ án",
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
fun ToolBarAssign(title: String, doLogout: () -> Unit) {
    TopAppBar(backgroundColor = colorResource(id = R.color.text_color) ,
        title = {
            Text(
                text = title,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
            )
        },
        actions = {
            Text(
                text = "Đăng xuất",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .clickable {
                        doLogout()
                    }
                    .padding(end = 12.dp)
            )
        })

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