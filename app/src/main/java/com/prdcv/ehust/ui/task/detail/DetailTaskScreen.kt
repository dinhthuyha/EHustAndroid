package com.prdcv.ehust.ui.task.detail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.prdcv.ehust.R
import com.prdcv.ehust.extension.getFileName
import com.prdcv.ehust.extension.getType
import com.prdcv.ehust.extension.openInputStream
import com.prdcv.ehust.model.Attachment
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.ui.compose.BGBottomBar
import com.prdcv.ehust.ui.compose.Button
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Purple500
import com.prdcv.ehust.ui.task.detail.state.TaskDetailScreenState
import com.prdcv.ehust.viewmodel.DetailTaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bouncycastle.asn1.x500.style.RFC4519Style.name
import java.io.InputStream

lateinit var navController: NavController

@Composable
fun DetailTask(
    viewModel: DetailTaskViewModel,
    mNavController: NavController
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
                title = uiState.taskTitle.value,
                onCloseScreen = { (navController.popBackStack()) },
                onEditTask = { uiState.readOnly.value = false })
        }, bottomBar = {
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
        }) {
            if (uiState.isLoading.value) {
                LoadingAnimation()
            } else {
                navController = mNavController
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(it),
                    state = lazyListState
                ) {
                    item {
                        RowDescription(
                            text = uiState.taskDescription,
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
                    items(items = uiState.taskAttachments.value) { t -> AttachmentRow(t) }
                    item {
                        if (!uiState.readOnly.value) {
                            Column(modifier = Modifier.padding(start = 25.dp)) {
                                if (uiState.progressBarVisible.value) {
                                    LinearProgressIndicator(progress = uiState.uploadProgress.value, color = Button)
                                }
                                ButtonAddFile(viewModel::onAttachmentSelected)
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                    item {
                        CommentSection(viewModel, numberCommentShow)
                    }

                    items(items = uiState.taskComments.value.takeLast(numberCommentShow.value)) { cmt ->
                        RowComment(comment = cmt)
                    }

                    if (!uiState.readOnly.value) {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Button(
                                    onClick = viewModel::updateTaskDetails,
                                    content = {
                                        Text(
                                            text = "Submit",
                                            style = MaterialTheme.typography.button,
                                            color = White
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Button
                                    )
                                )
                                Spacer(modifier = Modifier.height(60.dp))
                            }
                        }
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
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 10.dp,
    circleColor: Color = MaterialTheme.colors.primary,
    spaceBetween: Dp = 8.dp,
    travelDistance: Dp = 8.dp
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val circles = listOf(
            remember { Animatable(initialValue = 0f) },
            remember { Animatable(initialValue = 0f) },
            remember { Animatable(initialValue = 0f) }
        )

        circles.forEachIndexed { index, animatable ->
            LaunchedEffect(key1 = animatable) {
                delay(index * 100L)
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = 1200
                            0.0f at 0 with LinearOutSlowInEasing
                            1.0f at 300 with LinearOutSlowInEasing
                            0.0f at 600 with LinearOutSlowInEasing
                            0.0f at 1200 with LinearOutSlowInEasing
                        },
                        repeatMode = RepeatMode.Restart
                    )
                )
            }
        }

        val circleValues = circles.map { it.value }
        val distance = with(LocalDensity.current) { travelDistance.toPx() }

        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(spaceBetween)
        ) {
            circleValues.forEach { value ->
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .graphicsLayer {
                            translationY = -value * distance
                        }
                        .background(
                            color = circleColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}


@Composable
fun AttachmentRow(attachment: Attachment) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            start = 30.dp, end = 10.dp, bottom = 8.dp,
        )
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_file), contentDescription = "")
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = attachment.filename ?: "", style = MaterialTheme.typography.caption)

    }
}

@Composable
fun RowTaskSetup(
    viewModel: DetailTaskViewModel
) {
    val uiState = viewModel.uiState
    val activity = LocalContext.current as? AppCompatActivity
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
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)) {
                    RowElementSetupTask(
                        value = uiState.uiDateRange,
                        title = "",
                        readOnly = mutableStateOf(true),
                        idIcon = R.drawable.ic_date,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            if (uiState.readOnly.value) return@clickable
                            val picker = MaterialDatePicker.Builder
                                .dateRangePicker()
                                .setTitleText("Select dates")
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
                        modifier = Modifier.weight(0.45f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        RowElementSetupTask(
                            value = uiState.taskEstimateTime,
                            title = "Estimate time",
                            idIcon = R.drawable.ic_time,
                            "Hours",
                            readOnly = uiState.readOnly
                        )
                    }

                    Column(
                        modifier = Modifier.weight(0.55f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        RowElementSetupTask(
                            value = uiState.taskSpendTime,
                            title = "Spend time",
                            idIcon = R.drawable.ic_spendtime,
                            "Hours",
                            readOnly = uiState.readOnly
                        )
                    }

                }
                Row {
                    Column(
                        modifier = Modifier.weight(0.45f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        RowElementSetupTask(
                            value = uiState.taskProgress,
                            title = "Done",
                            idIcon = R.drawable.ic_done,
                            readOnly = uiState.readOnly,
                            trailingTitle = "%"
                        )
                    }
                    Column(
                        modifier = Modifier.weight(0.55f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        RowElementSetupTask(
                            value = uiState.taskAssignee,
                            title = "Assignee",
                            idIcon = R.drawable.ic_assignee,
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = uiState.readOnly,
                            keyboardType = KeyboardType.Text
                        )
                    }

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
fun RowDescription(
    text: MutableState<String>,
    readOnly: MutableState<Boolean>
) {
    val focusManager = LocalFocusManager.current

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
        ) {
            OutlinedTextField(
                value = text.value,
                onValueChange = {
                    text.value = it
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

@Composable
fun RowComment(comment: Comment) {
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
            Text(
                text = comment.nameUserPost ?: "",
                fontWeight = FontWeight.W400,
                style = MaterialTheme.typography.subtitle1,
                color = Button
            )
            Text(text = comment.content, style = MaterialTheme.typography.caption)
        }

    }
}


@Composable
fun ToolBar(title: String?, onEditTask: () -> Unit, onCloseScreen: () -> Unit) {

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
                Icon(Icons.Filled.Edit,
                    contentDescription = "Localized description",
                    modifier = Modifier.clickable { onEditTask.invoke() }
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
                modifier = Modifier
                    .size(width = 35.dp, height = 35.dp)
                    .weight(1f),
                tint = DarkGray
            )
            OutlinedTextField(
                value = txt,
                onValueChange = { txt = it },
                modifier = Modifier
                    .border(BorderStroke(0.5.dp, Gray), CircleShape)
                    .fillMaxHeight()
                    .weight(8f),
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
                }, enabled = txt.isNotBlank(), modifier = Modifier
                    .weight(1f)
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
