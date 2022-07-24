package com.prdcv.ehust.ui.task.detail

import android.text.format.DateUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.prdcv.ehust.R
import com.prdcv.ehust.extension.*
import com.prdcv.ehust.model.Attachment
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.ui.compose.*
import com.prdcv.ehust.ui.task.detail.state.TaskDetailScreenState
import com.prdcv.ehust.viewmodel.DetailTaskViewModel
import com.prdcv.ehust.viewmodel.TaskStatus
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
                title = if (viewModel.isNewTask) "Tạo công việc" else "Chi tiết công việc",
                isEditing = !uiState.readOnly.value,
                onCloseScreen = { (navController.popBackStack()) },
                onEditTask = { uiState.readOnly.value = false },
                onSaveTask = viewModel::saveTask,
                onDiscardTask = viewModel::getDetailTask
            )
        }, bottomBar = {
            if (!viewModel.isNewTask) {
                Column {
                    BottomBarAttachment(uiState)
                    BottomBarComment(onSendClick = {
                        coroutineScope.launch {
                            viewModel.postComment(it)
                            lazyListState.layoutInfo.run {
                                while (visibleItemsInfo.last().index < totalItemsCount) {
                                    lazyListState.animateScrollToItem(totalItemsCount)
                                }
                            }
                        }
                    }, onAttachmentSelected = viewModel::onAttachmentSelected)
                }
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

                    if (!uiState.readOnly.value) {
                        return@LazyColumn
                    }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                    item {
                        CommentSection(viewModel, numberCommentShow)
                    }

                    items(items = uiState.taskComments.value.takeLast(numberCommentShow.value)) { cmt ->
                        RowComment(comment = cmt) { filename, filePath ->
                            navController.navigate(
                                DetailTaskFragmentDirections.actionDetailTaskFragmentToAttachmentViewerFragment(
                                    filePath,
                                    filename
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomBarAttachment(uiState: TaskDetailScreenState) {
    uiState.fileToUpload.value?.let {
        Divider(thickness = .8.dp)
        AttachmentRow(Attachment(it.filename)) {}
    }
    if (uiState.progressBarVisible.value) {
        LinearProgressIndicator(
            progress = uiState.uploadProgress.value,
            color = Button,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CommentSection(
    viewModel: DetailTaskViewModel,
    numberCommentShow: MutableState<Int>
) {
    val uiState = viewModel.uiState
    val taskComments by uiState.taskComments

    Column(modifier = Modifier.fillMaxWidth()) {
        LaunchedEffect(key1 = Unit) {
            viewModel.getComments()
        }
        Text(
            text = uiState.commentSectionTitle,
            Modifier.padding(start = 25.dp, bottom = 12.dp),
            color = Black,
            fontWeight = FontWeight.Bold
        )
        if (taskComments.size > 4) {
            Text(
                text = "Xem bình luận cũ hơn",
                Modifier
                    .padding(start = 25.dp, bottom = 12.dp)
                    .clickable {
                        if (numberCommentShow.value == 0) {
                            numberCommentShow.value = 4
                        } else {
                            when (taskComments.size / numberCommentShow.value > 1) {
                                true -> {
                                    val n =
                                        taskComments.size / numberCommentShow.value
                                    numberCommentShow.value = 4 * n
                                }
                                false -> {
                                    numberCommentShow.value =
                                        taskComments.size
                                }
                            }
                        }
                    },
                color = Purple500,
                style = MaterialTheme.typography.caption,
            )
            Text(
                text = "Thu gọn",
                Modifier
                    .padding(start = 25.dp, bottom = 12.dp)
                    .clickable {
                        numberCommentShow.value = 0
                    },
                color = Purple500,
                style = MaterialTheme.typography.caption,
            )
        }
        if (!uiState.isLoading.value && taskComments.isEmpty()) {
            Text(
                text = "Chưa có bình luận",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = LightGray,
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@Composable
fun AttachmentRow(attachment: Attachment, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                start = 60.dp, end = 10.dp, bottom = 8.dp,
            )
            .clickable { onClick() }
            .background(
                color = VeryLightGray,
                shape = RoundedCornerShape(3.dp)
            )
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_file), contentDescription = "")
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = attachment.filename ?: "",
            style = MaterialTheme.typography.caption,
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
            text = "Thiết lập",
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
                        readOnly = uiState.readOnly,
                        idIcon = R.drawable.ic_date,
                        modifier = Modifier
                            .heightIn(20.dp, 50.dp)
                            .fillMaxWidth()
                    )
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable {
                            if (uiState.readOnly.value) return@noRippleClickable
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
                Divider(thickness = .5f.dp)
                Row {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.heightIn(20.dp, 50.dp)
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
                Divider(thickness = .5f.dp)
                Row {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.heightIn(20.dp, 50.dp)
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
                Divider(thickness = .5f.dp)
                Row {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.heightIn(20.dp, 50.dp)
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
                Divider(thickness = .5f.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .editableHighlight(!uiState.readOnly.value)
                            .fillMaxSize(),
                    ) {
                        SpinnerStatusTask(
                            label = "Trạng thái công việc: ",
                            options = uiState.listStatusTask,
                            selectedOption = uiState.taskStatus,
                            onItemClick = viewModel::onStatusTaskSelected
                        )
                    }
                    if (uiState.readOnly.value) {
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable {}) {}
                    }
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpinnerStatusTask(
    label: String,
    options: List<TaskStatus>,
    selectedOption: MutableState<TaskStatus?>,
    onItemClick: (TaskStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(10.dp)
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption.value?.text ?: "",
            onValueChange = {},
            label = { Text(label) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedBorderColor = Gray,
                unfocusedBorderColor = LightGray,
                focusedLabelColor = Gray,
                focusedTrailingIconColor = Gray
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
                    Text(text = selectionOption.text)
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
                .editableHighlight(!readOnly.value)
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
            text = "Mô tả",
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
                        .editableHighlight(!readOnly.value)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .editableHighlight(!readOnly.value)
                    ,
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
fun RowComment(comment: Comment, onAttachmentClick: (String?, String?) -> Unit = { _, _ -> }) {
    Column {
        Row(
            modifier = Modifier
                .padding(top = 5.dp, start = 15.dp)
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
        comment.attachments?.firstOrNull()?.let {
            AttachmentRow(it) { onAttachmentClick(it.filename, it.filePath) }
        }
    }
}


@Composable
fun ToolBar(
    title: String?,
    isEditing: Boolean,
    onEditTask: () -> Unit,
    onCloseScreen: () -> Unit,
    onSaveTask: () -> Unit,
    onDiscardTask: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = title ?: "Detail task")
        },
        navigationIcon = {
            IconButton(onClick = {
                if (isEditing) onDiscardTask() else onCloseScreen()
            }) {
                Icon(
                    imageVector = if (isEditing) Icons.Filled.Close else Icons.Filled.ArrowBack,
                    contentDescription = "Menu Btn"
                )
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

@Composable
fun BottomBarComment(
    onSendClick: (String) -> Unit = {},
    onAttachmentSelected: (inputStream: InputStream?, filename: String?, contentType: String?) -> Unit
) {
    BottomAppBar(elevation = 4.dp, backgroundColor = BGBottomBar) {
        var txt by remember { mutableStateOf("") }

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
                        text = "Viết bình luận ...",
                        color = Gray,
                        fontWeight = FontWeight.W400,
                        fontSize = 15.sp
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { launcher.launch("*/*") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_attach_file),
                            contentDescription = null
                        )
                    }
                }
            )

            IconButton(
                onClick = {
                    onSendClick(txt)
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
        onSaveTask = {},
        onDiscardTask = {}
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

@Preview(showBackground = true)
@Composable
private fun AttachmentRowPreview() {
    AttachmentRow(attachment = Attachment("file_name.pdf")) {}
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    BottomBarComment(onAttachmentSelected = { _, _, _ -> })
}