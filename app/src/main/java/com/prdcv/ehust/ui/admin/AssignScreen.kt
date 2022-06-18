package com.prdcv.ehust.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prdcv.ehust.R
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Purple500

@Preview(showBackground = true)
@Composable
fun AssignScreen() {
    DefaultTheme {
        Scaffold() {
            ToolBarAssign(title = "Trang chủ ")
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spinner(label = "Danh sách project", options = listOf("Proejct I", "Proejct II"))
                Spacer(modifier = Modifier.height(16.dp))
                Spinner(label = "Danh sách project", options = listOf("Proejct I", "Proejct II"))
                Spacer(modifier = Modifier.height(16.dp))
                Spinner(label = "Danh sách project", options = listOf("Proejct I", "Proejct II"))
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
fun Spinner(label: String, options: List<String>) {
    val focusManager = LocalSoftwareKeyboardController.current
    var expanded = remember { mutableStateOf(false) }
    var selectedOptionText = remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
            focusManager?.hide()
        }
    ) {
        OutlinedTextField(
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
