package com.prdcv.ehust.ui.task.detail

import android.text.format.DateUtils
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.prdcv.ehust.R
import com.prdcv.ehust.extension.findActivity
import com.prdcv.ehust.extension.getFileName
import com.prdcv.ehust.extension.getType
import com.prdcv.ehust.extension.openInputStream
import com.prdcv.ehust.model.Attachment
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.admin.SpinnerStudent
import com.prdcv.ehust.ui.compose.BGBottomBar
import com.prdcv.ehust.ui.compose.Button
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Purple500
import com.prdcv.ehust.viewmodel.DetailTaskViewModel
import kotlinx.coroutines.launch
import java.io.InputStream
import java.sql.Timestamp


@Composable
fun DetailTask(
    viewModel: DetailTaskViewModel,
    navController: NavController
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getDetailTask()
    }
    val uiState = viewModel.uiState
    val numberCommentShow: MutableState<Int> = mutableStateOf(4)
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    DefaultTheme {
        Scaffold(topBar = {
            ToolBar(
                title = "Chi tiết công việc",
                isEditing = !uiState.readOnly.value,
                onCloseScreen = { (navController.popBackStack()) },
                onEditTask = { uiState.readOnly.value = false },
                onSaveTask = viewModel::saveTask
            )
        }, bottomBar = {
            if (!viewModel.isNewTask) {
                BottomBarComment(onSendClick = {
                    viewModel.postComment(it)
                    coroutineScope.launch {
                        lazyListState.layoutInfo.run {
                            while (visibleItemsInfo.last().index < totalItemsCount) {
                                lazyListState.animateScrollToItem(totalItemsCount)
                            }
                        }
                    }
                })
            }
        }) {
            if (uiState.isLoading.value) {
                LoadingAnimation()
            } else {
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    state = lazyListState
                ) {
                    item {
                        RowTaskDescription(
                            taskTitle = uiState.taskTitle,
                            taskDescription = uiState.taskDescription,
                            uiState.readOnly
                        )
                    }
                    item {
                        RowTaskSetup(viewModel = viewModel)
                    }

                    item {
                        LaunchedEffect(key1 = Unit) {
                            viewModel.getAttachments()
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(Modifier.padding(start = 15.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_attach_file),
                                contentDescription = ""
                            )
                            Text(
                                text = "Attachments",
                                Modifier.padding(start = 15.dp, bottom = 12.dp),
                                color = Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    items(items = uiState.taskAttachments.value) { t ->
                        AttachmentRow(t) {
                            navController.navigate(
                                DetailTaskFragmentDirections.actionDetailTaskFragmentToAttachmentViewerFragment(
                                    t.filePath,
                                    t.filename
                                )
                            )
                        }
                    }
                    item {
                        if (!uiState.readOnly.value) {
                            Column(modifier = Modifier.padding(start = 25.dp)) {
                                if (uiState.progressBarVisible.value) {
                                    LinearProgressIndicator(
                                        progress = uiState.uploadProgress.value,
                                        color = Button
                                    )
                                }
                                ButtonAddFile(viewModel::onAttachmentSelected)
                            }
                        }
                    }
                    if (!uiState.readOnly.value) {
                        return@LazyColumn
                    }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                    item {
                        CommentSection(viewModel, numberCommentShow)
                    }

                    items(items = uiState.taskComments.value.takeLast(numberCommentShow.value)) { cmt ->
                        RowComment(comment = cmt)
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentSection(
    viewModel: DetailTaskViewModel,
    numberCommentShow: MutableState<Int>
) {
    val uiState = viewModel.uiState
    Column(modifier = Modifier.fillMaxWidth()) {
        LaunchedEffect(key1 = Unit) {
            viewModel.getComments()
        }
        Text(
            text = "Comments",
            Modifier.padding(start = 25.dp, bottom = 12.dp),
            color = Black,
            fontWeight = FontWeight.Bold
        )
        if (uiState.taskComments.value.size > 4) {
            Text(
                text = "See Previous replies",
                Modifier
                    .padding(start = 25.dp, bottom = 12.dp)
                    .clickable {
                        if (numberCommentShow.value == 0) {
                            numberCommentShow.value = 4
                        } else {
                            when (uiState.taskComments.value.size / numberCommentShow.value > 1) {
                                true -> {
                                    val n =
                                        uiState.taskComments.value.size / numberCommentShow.value
                                    numberCommentShow.value = 4 * n
                                }
                                false -> {
                                    numberCommentShow.value =
                                        uiState.taskComments.value.size
                                }
                            }
                        }
                    },
                color = Purple500,
                style = MaterialTheme.typography.caption,
            )
            Text(
                text = "Collapse all",
                Modifier
                    .padding(start = 25.dp, bottom = 12.dp)
                    .clickable {
                        numberCommentShow.value = 0
                    },
                color = Purple500,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}

@Composable
fun ButtonAddFile(onAttachmentSelected: (inputStream: InputStream?, filename: String?, contentType: String?) -> Unit) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val file = context.openInputStream(uri)
                val type = context.getType(uri)
                val filename = context.getFileName(uri)
                onAttachmentSelected(file, filename, type)
            }
        }

    Button(
        onClick = {
            launcher.launch("*/*")
        },
        content = {
            Text(
                text = "Add file",
                style = MaterialTheme.typography.button,
                color = White
            )
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Button
        )
    )

}

@Composable
fun AttachmentRow(attachment: Attachment, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = 30.dp, end = 10.dp, bottom = 8.dp,
            )
            .clickable { onClick() }
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_file), contentDescription = "")
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = attachment.filename ?: "",
            style = MaterialTheme.typography.caption,
            color = Blue,
            textDecoration = TextDecoration.Underline
        )

    }
}

@Composable
fun RowTaskSetup(
    viewModel: DetailTaskViewModel
) {
    val uiState = viewModel.uiState
    val activity = LocalContext.current.findActivity() as? AppCompatActivity
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
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    RowElementSetupTask(
                        value = uiState.uiDateRange,
                        title = "Thời gian thực hiện",
                        readOnly = mutableStateOf(true),
                        idIcon = R.drawable.ic_date,
                        modifier = Modifier
                            .heightIn(20.dp, 50.dp)
                            .fillMaxWidth()
                    )
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            if (uiState.readOnly.value) return@clickable
                            val picker = MaterialDatePicker.Builder
                                .dateRangePicker()
                                .setTitleText("Chọn thời gian")
                                .apply {
                                    uiState
                                        .getSelectedDates()
                                        ?.let(::setSelection)
                                }
                                .build()

                            picker.addOnPositiveButtonClickListener {
                                uiState.updateSelectedDates(it)
                            }

                            activity?.let {
                                picker.show(it.supportFragmentManager, picker.toString())
                            }
                        }) {}
                }

                Row {
                    Column(
                        horizontalAlignment = Alignment.Start, modifier = Modifier.heightIn(20.dp, 50.dp)
                    ) {
                        RowElementSetupTask(
                            value = uiState.taskEstimateTime,
                            title = "Số giờ hoàn thành công việc",
                            idIcon = R.drawable.ic_time,
                            "Hours",
                            readOnly = uiState.readOnly,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
                Row {
                    Column(
                        horizontalAlignment = Alignment.Start, modifier = Modifier.heightIn(20.dp, 50.dp)
                    ) {
                        RowElementSetupTask(
                            value = uiState.taskProgress,
                            title = "Tiến độ hoàn thành",
                            idIcon = R.drawable.ic_done,
                            readOnly = uiState.readOnly,
                            modifier = Modifier.fillMaxWidth(),
                            trailingTitle = "%"
                        )
                    }


                }
                Row {
                    Column(
                        horizontalAlignment = Alignment.Start, modifier = Modifier.heightIn(20.dp, 50.dp)
                    ) {
                        RowElementSetupTask(
                            value = uiState.taskAssignee,
                            title = "Người tạo task",
                            idIcon = R.drawable.ic_assignee,
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = uiState.readOnly,
                            keyboardType = KeyboardType.Text
                        )
                    }
                }
                Row {
                    Column(
                        horizontalAlignment = Alignment.Start, modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        SpinnerStatusTask(
                            label = "Trạng thái công việc: ",
                            options = uiState.listStatusTask,
                            selectedOption = uiState.selectedStatusTask,
                            onItemClick = viewModel::onStatusTaskSelected
                        )

                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }

        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpinnerStatusTask(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onItemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption?: "",
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
                    Text(text = selectionOption)
                }
            }
        }
    }
}
@Composable
fun RowElementSetupTask(
    value: MutableState<String>,
    title: String,
    idIcon: Int? = null,
    trailingTitle: String? = null,
    modifier: Modifier = Modifier,
    readOnly: MutableState<Boolean>,
    keyboardType: KeyboardType = KeyboardType.Number

) {
    val focusManager = LocalFocusManager.current
    Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = value.value,
            maxLines = 1,
            onValueChange = { value.value = it },
            modifier = modifier
                .width(95.dp)
                .then(modifier),
            colors =
            TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Transparent,
                unfocusedBorderColor = Transparent
            ),
            textStyle = TextStyle(fontWeight = FontWeight.W400),
            placeholder = {
                Text(
                    text = title,
                    color = Gray,
                    fontWeight = FontWeight.W400,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.offset(y = (5).dp)
                )
            },
            leadingIcon = {
                idIcon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = "",
                        modifier = Modifier.size(width = 25.dp, height = 25.dp),
                        tint = DarkGray
                    )
                }
            },
            readOnly = readOnly.value,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        trailingTitle?.let {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 2.dp)
                    .offset(y = (-2).dp),
                text = trailingTitle,
                style = MaterialTheme.typography.caption
            )
        }
    }
}


@Composable
fun RowTaskDescription(
    taskTitle: MutableState<String>,
    taskDescription: MutableState<String>,
    readOnly: MutableState<Boolean>
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Description",
            Modifier.padding(start = 15.dp, bottom = 12.dp),
            fontWeight = FontWeight.Bold,
        )
        Card(
            elevation = 4.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                BasicTextField(
                    value = taskTitle.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .padding(start = 5.dp),
                    onValueChange = { taskTitle.value = it },
                    readOnly = readOnly.value,
                    textStyle = TextStyle(fontWeight = FontWeight.SemiBold),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    })
                )
                Divider(thickness = 0.5.dp)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = taskDescription.value,
                    onValueChange = {
                        taskDescription.value = it
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Transparent,
                        unfocusedBorderColor = Transparent
                    ),
                    readOnly = readOnly.value,
                    textStyle = TextStyle(fontWeight = FontWeight.W400),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                    })
                )
            }
        }

    }
}

@Composable
fun RowComment(comment: Comment) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_ava),
            contentDescription = "",
            modifier = Modifier
                .size(width = 35.dp, height = 35.dp),
            tint = DarkGray
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier
                .padding(end = 20.dp, bottom = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.nameUserPost ?: "",
                    fontWeight = FontWeight.W400,
                    style = MaterialTheme.typography.subtitle1,
                    color = Button
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = comment.timestampFormatted,
                    fontWeight = FontWeight.W400,
                    style = MaterialTheme.typography.subtitle1.copy(fontSize = 12.sp),
                    color = LightGray
                )
            }
            Text(text = comment.content, style = MaterialTheme.typography.caption)
        }

    }
}


@Composable
fun ToolBar(
    title: String?,
    isEditing: Boolean,
    onEditTask: () -> Unit,
    onCloseScreen: () -> Unit,
    onSaveTask: () -> Unit
) {

    TopAppBar(
        title = {
            Text(text = title ?: "Detail task")
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Menu Btn",
                    modifier = Modifier.clickable {
                        onCloseScreen.invoke()
                    })
            }
        },
        backgroundColor = colorResource(id = R.color.text_color),
        contentColor = White,
        elevation = 2.dp,
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(if (isEditing) Icons.Filled.Check else Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        when (isEditing) {
                            true -> onSaveTask()
                            false -> onEditTask()
                        }
                    }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BottomBarComment(onSendClick: ((String) -> Unit)? = null) {
    BottomAppBar(elevation = 4.dp, backgroundColor = BGBottomBar) {
        var txt by remember { mutableStateOf("") }

        Row(
            modifier = Modifier.padding(top = 3.dp, bottom = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_ava),
                contentDescription = "",
                modifier = Modifier.size(width = 35.dp, height = 35.dp),
                tint = DarkGray
            )
            OutlinedTextField(
                value = txt,
                onValueChange = { txt = it },
                modifier = Modifier
                    .border(BorderStroke(0.5.dp, Gray), CircleShape)
                    .fillMaxHeight()
                    .weight(1f),
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
                        style = MaterialTheme.typography.h3
                    )
                },
            )

            IconButton(
                onClick = {
                    onSendClick?.invoke(txt)
                    txt = ""
                }, enabled = txt.isNotBlank()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "",
                    tint = DarkGray,
                    modifier = Modifier.size(width = 35.dp, height = 35.dp)
                )
            }
        }
    }
}

private val Comment.timestampFormatted: String
    get() = timestamp?.let {
        DateUtils.getRelativeTimeSpanString(it.time).toString()
    } ?: ""

@Preview(showBackground = true)
@Composable
private fun ToolBarPreview() {
    ToolBar(
        title = "Tiêu đề",
        isEditing = true,
        onEditTask = {},
        onCloseScreen = {},
        onSaveTask = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun DescriptionRowPreview() {
    val title = mutableStateOf("Mockup UI")
    val des =
        mutableStateOf("Thiết kế giao diện các màn hình Home, Search, Project, Topic, Calendar, Profile, News")
    RowTaskDescription(taskTitle = title, taskDescription = des, readOnly = mutableStateOf(false))
}

@Preview(showBackground = true)
@Composable
private fun CommentRowPreview() {
    RowComment(
        comment = Comment(
            content = "comment content \uD83E\uDD2A",
            nameUserPost = "User name",
            timestamp = Timestamp.valueOf("2022-07-10 04:12:10")
        )
    )
}