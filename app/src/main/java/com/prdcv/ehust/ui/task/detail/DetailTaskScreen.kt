package com.prdcv.ehust.ui.task.detail


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import com.prdcv.ehust.ui.compose.Button
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prdcv.ehust.R
import com.prdcv.ehust.ui.compose.BGBottomBar
import com.prdcv.ehust.ui.compose.DefaultTheme



@Preview(showBackground = true)
@Composable
fun DetailTask() {
    DefaultTheme {
        Scaffold(topBar = { ToolBar() }, bottomBar = { BottomBarComment()}) {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.verticalScroll(rememberScrollState())) {
                RowDescription()
                RowTaskSetup()
                RowAttachFile()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Comments",
                    Modifier.padding(start = 25.dp, bottom = 12.dp),
                    color = Black,
                    fontWeight = FontWeight.Bold
                )
                RowComment()
                RowComment()
                RowComment()
                RowComment()
                RowComment()
            }

        }
    }
}

@Composable
fun RowAttachFile() {
    Spacer(modifier = Modifier.height(15.dp))
    Row(Modifier.padding(start = 15.dp)) {
        Icon(painter = painterResource(id = R.drawable.ic_attach_file), contentDescription = "")
        Text(
            text = "Attach file",
            Modifier.padding(start = 15.dp, bottom = 12.dp),
            color = Black,
            fontWeight = FontWeight.Bold
        )
    }

    AttachFile()
    AttachFile()
    AttachFile()
    Row(modifier = Modifier.padding(start = 25.dp)) {
        Button(
            onClick = { /*TODO*/ },
            content = { Text(text = "Add file", fontSize = 12.sp) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Button,
                contentColor = White
            )
        )
    }

}

@Preview
@Composable
fun AttachFile() {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 30.dp, end = 10.dp, bottom = 8.dp,
        )
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_file), contentDescription = "")
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = "kindpng_3651626.png", fontWeight = FontWeight.W400, fontSize = 12.sp)

    }
}

@Preview
@Composable
fun RowTaskSetup() {
    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Task setup",
            Modifier.padding(start = 15.dp, bottom = 12.dp),
            color = Black,
            fontWeight = FontWeight.Bold
        )
        Card(
            elevation = 4.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }

        ) {
            Column {
                OutlinedTextField(
                    value = "date picker",
                    onValueChange = {},
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Transparent,
                        unfocusedBorderColor = Transparent
                    ),
                    textStyle = TextStyle(fontWeight = FontWeight.W400, fontSize = 12.sp),
                    readOnly = true,
                    modifier = Modifier.defaultMinSize(minHeight = 1.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_date),
                            contentDescription = "",
                            modifier = Modifier.size(width = 25.dp, height = 25.dp),
                            tint = DarkGray
                        )
                    },
                )
                RowElementSetupTask(title = "Estimate time", idIcon = R.drawable.ic_time, "Hours")
                RowElementSetupTask(title = "Spend time", idIcon = R.drawable.ic_spendtime,  "Hours")
                RowElementSetupTask(title = "Done", idIcon = R.drawable.ic_done)
                RowElementSetupTask(title = "Assignee", idIcon = R.drawable.ic_assignee)
            }

        }

    }
}


@Composable
fun RowElementSetupTask(title: String, idIcon: Int, trailingTitle: String?= null) {
    var txt = remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = txt.value,
            onValueChange = { txt.value = it },
            modifier = Modifier.defaultMinSize(minHeight = 2.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Transparent,
                unfocusedBorderColor = Transparent
            ),
            textStyle = TextStyle(fontWeight = FontWeight.W400),
            placeholder = {
                Text(
                    text = title,
                    color = Gray,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = idIcon),
                    contentDescription = "",
                    modifier = Modifier.size(width = 25.dp, height = 25.dp),
                    tint = DarkGray
                )
            },
        )
        Text(text = trailingTitle?: "", fontWeight = FontWeight.W400, fontSize = 12.sp)
    }
}

@Preview
@Composable
fun RowDescription() {
    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Desciption",
            Modifier.padding(start = 15.dp, bottom = 12.dp),
            color = Black,
            fontWeight = FontWeight.Bold
        )
        Card(
            elevation = 4.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }

        ) {
            OutlinedTextField(
                value = "The top app bar provides content and actions related to the current screen. It’s used for branding, screen titles, navigation, and actions.",
                onValueChange = {},
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Transparent,
                    unfocusedBorderColor = Transparent
                ),
                textStyle = TextStyle(fontWeight = FontWeight.W400),
                readOnly = true
            )
        }

    }
}

@Preview
@Composable
fun RowComment() {

        Row(
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 15.dp),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_ava),
                contentDescription = "",
                modifier = Modifier
                    .size(width = 35.dp, height = 35.dp)
                ,
                tint = DarkGray
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier
               .padding( end = 20.dp, bottom = 5.dp)) {
                Text(text = "Hà Đinh",fontWeight = FontWeight.W400, fontSize = 13.sp, color = Button)
                Text(text = "Chinh lai table User", fontWeight = FontWeight.W400, fontSize = 12.sp)
            }

        }


}

@Preview
@Composable
fun ToolBar() {
    TopAppBar(
        title = {
            Text(text = "Thiết kế database")
        },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Menu Btn")
            }
        },
        backgroundColor = colorResource(id = R.color.text_color),
        contentColor = Color.White,
        elevation = 2.dp,
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(Icons.Filled.Edit, contentDescription = "Localized description")
            }
        }
    )
}

@Preview
@Composable
fun BottomBarComment() {
    BottomAppBar(elevation = 4.dp, backgroundColor = BGBottomBar) {
        val txt = remember {
            mutableStateOf("")
        }

        Row(
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_ava),
                contentDescription = "",
                modifier = Modifier
                    .size(width = 35.dp, height = 35.dp)
                    .weight(1f),
                tint = DarkGray
            )
            OutlinedTextField(
                value = txt.value,
                onValueChange = { txt.value = it },
                modifier = Modifier
                    .border(BorderStroke(0.5.dp, Gray), CircleShape)
                    .weight(9f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Transparent,
                    unfocusedBorderColor = Transparent
                ),
                textStyle = TextStyle(fontWeight = FontWeight.W400),
                placeholder = {
                    Text(
                        text = "Comment ...",
                        color = Gray,
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp
                    )
                },

                )
        }


    }

}


