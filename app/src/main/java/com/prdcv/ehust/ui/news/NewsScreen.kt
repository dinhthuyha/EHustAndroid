package com.prdcv.ehust.ui.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.prdcv.ehust.data.model.StatusNotification
import com.hadt.ehust.model.TypeNotification
import com.prdcv.ehust.R
import com.prdcv.ehust.common.State
import com.prdcv.ehust.data.model.News
import com.prdcv.ehust.ui.ShareViewModel
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Shapes
import com.prdcv.ehust.ui.compose.UNREAD
import com.prdcv.ehust.ui.task.Tag
import com.prdcv.ehust.ui.task.detail.TaskDetailArgs

@Composable
fun ToolBar(
    title: String?,
    isNotificationProject: Boolean = false,
    onClearNotification: () -> Unit ={}
) {
 val alpha = if (isNotificationProject) 1f else 0f
    TopAppBar(
        title = {
            Text(text = title ?: "Detail task")
        },
        backgroundColor = colorResource(id = R.color.text_color),
        contentColor = Color.White,
        elevation = 2.dp,
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(imageVector = Icons.Filled.Delete ,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        when (isNotificationProject) {
                            true -> onClearNotification()
                        }
                    }.alpha(alpha)
                )
            }
        }
    )
}
@Composable
fun NewsScreen(viewModel: ShareViewModel = viewModel(), typeNoti: TypeNotification, nav: NavController) {

    val navController = rememberNavController()
    val state by viewModel.newsState.collectAsState()
    val isNotificationProject = typeNoti == TypeNotification.TYPE_PROJECT
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getNews(typeNoti)
    })

    DefaultTheme {
        Scaffold(
            topBar = { ToolBar(title = stringResource(id = R.string.new_title),isNotificationProject = isNotificationProject, onClearNotification = viewModel::clearNotificationRead) }
        ) {
            NavHost(navController = navController, "newsList") {
                composable("newsList") {
                    NewsList(state,
                        navController,
                        onRefresh = { viewModel.getNews(typeNoti) },
                        viewModel = viewModel,
                        typeNoti = typeNoti,
                        nav = nav
                        )
                }
                composable(
                    "newsDetails/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) {
                    it.arguments?.getInt("id")?.let {
                        (state as? State.Success)?.data?.firstOrNull { n -> n.id == it }
                    }?.let { news ->
                        NewsDetails(
                            news = news
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsDetails(
    news: News = News(
        1,
        stringResource(id = R.string.item_news_content),
        stringResource(id = R.string.item_news_content),
        "16-06-2022",
        TypeNotification.TYPE_NORMAL,
        StatusNotification.STATUS_UNREAD,"",0
    )
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = news.title, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.size(10.dp))
        Divider()
        Text(text = news.datePost, color = Color.LightGray)
        Spacer(modifier = Modifier.size(15.dp))
        Text(text = news.content)
    }
}

@Composable
private fun NewsList(
    state: State<List<News>>,
    navController: NavController,
    onRefresh: () -> Unit = {},
    viewModel: ShareViewModel,
    typeNoti: TypeNotification,
    nav: NavController
) {
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)

    SwipeRefresh(
        state = refreshState,
        onRefresh = onRefresh
    ) {
        when (val newsList = state) {
            is State.Success -> {
                refreshState.isRefreshing = false
                LazyColumn {
                    items(items = newsList.data) {
                        NewsRow(it, navController, viewModel, typeNoti, nav)
                    }
                }
            }
            is State.Loading -> refreshState.isRefreshing = true
            else -> {}
        }
    }
}

@Composable
fun NewsRow(
    news: News = News(
        1,
        stringResource(id = R.string.item_news_content),
        stringResource(id = R.string.item_news_content),
        "16-06-2022",
        TypeNotification.TYPE_NORMAL,
        StatusNotification.STATUS_UNREAD,"",0
    ),
    navController: NavController,
    viewModel: ShareViewModel,
    typeNoti: TypeNotification,
    nav: NavController
) {
    val bgColor = if (news.status == StatusNotification.STATUS_UNREAD) {
        UNREAD
    } else {
        Color.White
    }
    Card(
        elevation = 4.dp,
        shape = Shapes.small,
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 7.dp, bottom = 8.dp)
            .fillMaxWidth()
            .clickable {
                navController.currentBackStackEntry?.arguments?.apply {
                    putParcelable("itemNews", news)
                }
                if (typeNoti == TypeNotification.TYPE_PROJECT){
                    nav.navigate(NewsFragmentDirections.actionNewsFragmentToDetailTaskFragment(
                        TaskDetailArgs( idTask = news.idTask?:1)
                    ))
                }else {
                    navController.navigate("newsDetails/${news.id}")
                }

                viewModel.updateStatusNew(news.id?:0, typeNoti, StatusNotification.STATUS_READ)
            },
        backgroundColor = bgColor
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
                    .placeholder(visible = false)
            ) {
                Tag(status = "#hashtag")
                Text(text = news.datePost, fontSize = 12.sp, fontWeight = FontWeight.Light)
            }
            Text(
                text = news.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.placeholder(visible = false)
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = news.content,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.placeholder(visible = false)
            )
        }
    }
}
